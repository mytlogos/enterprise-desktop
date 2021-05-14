package com.mytlogos.enterprisedesktop.background.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytlogos.enterprisedesktop.tools.Utils;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class ServerDiscovery {

    private static final boolean isDev = true;
    private final int maxAddress = 50;
    private final ExecutorService executor = Executors.newFixedThreadPool(maxAddress, Utils.countingThreadFactory("Server-Discovery-"));

    @SuppressWarnings("java:S2142")
    Server discover(InetAddress broadcastAddress) {
        Set<Server> discoveredServer = Collections.synchronizedSet(new HashSet<>());

        List<Future<Server>> futures = new ArrayList<>();
        for (int i = 1; i < maxAddress; i++) {
            int local = i;
            // this is for emulator sessions,
            // as localhost udp server cannot seem to receive upd packets send from emulator
            futures.add(this.executor.submit(() -> this.discoverLocalNetworkServerPerTcp(local)));
        }
        
        final Thread discoverThread = new BroadcastDiscoverThread(broadcastAddress, discoveredServer);
        Utils.ignore(() -> {
            discoverThread.start();
            discoverThread.join(2000);
            discoverThread.interrupt();
        });
        
        for (Future<Server> serverFuture : futures) {
            Utils.ignore(() -> {
                if (serverFuture.isDone()) {
                    Server now = serverFuture.get(0, TimeUnit.SECONDS);
                    discoveredServer.add(now);
                } else {
                    serverFuture.cancel(true);
                }
            });
        }
        Server server = null;

        for (Server discovered : discoveredServer) {
            if (discovered != null && discovered.isLocal() && discovered.isDevServer() == isDev) {
                server = discovered;
                break;
            }
        }
        if (server == null) {
            try {
                server = this.executor.submit(this::discoverInternetServerPerUdp).get(1, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.executor.shutdownNow();
        return server;
    }

    private Server discoverLocalNetworkServerPerTcp(int local) {
        String ipv4 = "192.168.1." + local;
        Server server = new Server(ipv4, 3000, true, true);

        if (!server.isReachable()) {
            return null;
        }
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(LocalDateTime.class, new GsonAdapter.LocalDateTimeAdapter())
                    .create();
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(server.getAddress())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            BasicApi apiImpl = retrofit.create(BasicApi.class);
            Response<Boolean> devResponse = apiImpl.checkDev("api").execute();

            boolean isDev = devResponse.body() != null && devResponse.body();
            return new Server(ipv4, 3000, true, isDev);
        } catch (IOException ignored) {
        }
        return null;
    }

    private Server discoverInternetServerPerUdp() {
        // TODO: 27.07.2019 check if internet server is reachable
        return null;
    }

    private static class BroadcastDiscoverThread extends Thread {
        private final InetAddress broadcastAddress;
        private final Set<Server> discoveredServer;
        private DatagramSocket socket;

        private BroadcastDiscoverThread(InetAddress broadcastAddress, Set<Server> discoveredServer) {
            super("Local-Server-UDP-Discovery");
            this.broadcastAddress = broadcastAddress;
            this.discoveredServer = discoveredServer;
        }

        @Override
        public void run() {
            this.discoverLocalNetworkServerPerUdp(this.broadcastAddress, this.discoveredServer);
        }

        @Override
        public void interrupt() {
            super.interrupt();

            if (this.socket != null) {
                this.socket.close();
            }
        }

        /**
         * Modified Version from
         * <a href="https://michieldemey.be/blog/network-discovery-using-udp-broadcast/">
         * Network discovery using UDP Broadcast (Java)
         * </a>
         */
        private void discoverLocalNetworkServerPerUdp(InetAddress broadcastAddress, Set<Server> discoveredServer) {
            // Find the server using UDP broadcast
            //Open a random port to send the package
            try (DatagramSocket c = new DatagramSocket()) {
                this.socket = c;
                c.setBroadcast(true);
                sendMessages(broadcastAddress);
                waitResponses(discoveredServer);
            } catch (IOException ex) {
                if (!this.isInterrupted()) {
                    ex.printStackTrace();
                }
            }
        }

        private void sendMessages(InetAddress broadcastAddress)
                throws UnknownHostException, SocketException {
            byte[] sendData = "DISCOVER_SERVER_REQUEST_ENTERPRISE".getBytes();

            int udpServerPort = 3001;
            //Try the some 'normal' ip addresses first
            this.sendUDPPacket(sendData, udpServerPort, InetAddress.getLocalHost());
            this.sendUDPPacket(sendData, udpServerPort, InetAddress.getByName("255.255.255.255"));
            this.sendUDPPacket(sendData, udpServerPort, InetAddress.getByName("192.168.255.255"));

            for (int i = 1; i < 50; i++) {
                this.sendUDPPacket(sendData, udpServerPort, InetAddress.getByName("192.168.1." + i));
            }

            if (broadcastAddress != null) {
                this.sendUDPPacket(sendData, udpServerPort, broadcastAddress);
            }

            // Broadcast the message over all the network interfaces
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface networkInterface : interfaces) {
                if (!networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    // Send the broadcast package!
                    sendUDPPacket(sendData, udpServerPort, broadcast);
                }

            }
        }

        private void waitResponses(Set<Server> discoveredServer) throws IOException {
            //Wait for a response
            byte[] recvBuf = new byte[15000];

            while (!this.isInterrupted()) {
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                this.socket.receive(receivePacket);

                //Check if the message is correct
                String message = new String(receivePacket.getData()).trim();

                // the weird thing is if the message is over 34 bytes long
                // e.g. 'DISCOVER_SERVER_RESPONSE_ENTERPRISE' the last character will be cut off
                // either the node server does not send correctly
                // or java client does not receive correctly
                if ("ENTERPRISE_DEV".equals(message)) {
                    Server server = new Server(receivePacket.getAddress().getHostAddress(), 3000, true, true);
                    discoveredServer.add(server);
                } else if ("ENTERPRISE_PROD".equals(message)) {
                    Server server = new Server(receivePacket.getAddress().getHostAddress(), 3000, true, false);
                    discoveredServer.add(server);
                }
            }
        }

        private void sendUDPPacket(byte[] data, int port, InetAddress address) {
            try {
                this.socket.send(new DatagramPacket(data, data.length, address, port));
            } catch (IOException ignored) {
                // ignore IO exceptions
            }
        }
    }
}
