package com.mytlogos.enterprisedesktop.background.api;

import java.net.InetAddress;

public interface NetworkIdentificator {
    String getSSID();

    InetAddress getBroadcastAddress();
}
