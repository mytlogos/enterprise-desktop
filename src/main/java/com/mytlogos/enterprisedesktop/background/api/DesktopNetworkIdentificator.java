package com.mytlogos.enterprisedesktop.background.api;


import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;


public class DesktopNetworkIdentificator implements NetworkIdentificator {
    @Override
    public String getSSID() {
        try {
            for (NetworkInterface anInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (anInterface.isUp()) {
                    return anInterface.getDisplayName();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public InetAddress getBroadcastAddress() {
        NetworkInterface networkInterface = null;
        try {
            for (NetworkInterface anInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (anInterface.isUp()) {
                    networkInterface = anInterface;
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (networkInterface == null) {
            return null;
        }
        final List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
        if (interfaceAddresses.isEmpty()) {
            return null;
        }
        return interfaceAddresses.get(0).getBroadcast();
    }
}
