package com.mytlogos.enterprisedesktop.model;

public class ExternalUser {

    private final String uuid;

    private final String identifier;
    private final int type;

    public ExternalUser( String uuid,  String identifier, int type) {
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
