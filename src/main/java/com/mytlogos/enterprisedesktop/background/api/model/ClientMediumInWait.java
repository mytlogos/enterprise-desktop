package com.mytlogos.enterprisedesktop.background.api.model;

import com.mytlogos.enterprisedesktop.model.MediumInWait;

public class ClientMediumInWait implements MediumInWait {
    private final String title;
    private final int medium;
    private final String link;

    public ClientMediumInWait(String title, int medium, String link) {
        this.title = title;
        this.medium = medium;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public int getMedium() {
        return medium;
    }

    public String getLink() {
        return link;
    }
}
