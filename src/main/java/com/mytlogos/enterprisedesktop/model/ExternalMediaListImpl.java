package com.mytlogos.enterprisedesktop.model;

public class ExternalMediaListImpl extends MediaListImpl implements ExternalMediaList {
    private final String url;

    public ExternalMediaListImpl(String uuid, int listId, String name, int medium, String url, int size) {
        super(uuid, listId, name, medium, size);
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

}
