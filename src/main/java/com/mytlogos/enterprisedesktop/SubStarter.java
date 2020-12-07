package com.mytlogos.enterprisedesktop;

import javafx.application.Platform;

public class SubStarter {
    public static void main(String[] args) {
        // need to manually initialize the FX Toolkit here,
        // because Start main indirectly uses Platform.runlater
        Platform.startup(() -> System.out.println("FX started up"));
        Starter.main(args);
    }
}
