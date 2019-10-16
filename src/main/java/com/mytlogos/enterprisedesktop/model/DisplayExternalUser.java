package com.mytlogos.enterprisedesktop.model;

public class DisplayExternalUser {

    private final String uuid;

    private final String identifier;
    private final int type;

    public DisplayExternalUser(String uuid, String identifier, int type) {
        this.uuid = uuid;
        this.identifier = identifier;
        this.type = type;
    }


    public String getUuid() {
        return uuid;
    }


    public String getIdentifier() {
        return identifier;
    }

    public int getType() {
        return type;
    }
}
