package com.mytlogos.enterprisedesktop.background.api.model;

/**
 * API Model for LinkablePerson.
 * Enterprise Web API 1.0.2.
 */
public class LinkablePerson {
    private String name;
    private String link;

    public LinkablePerson(String name, String link) {
        this.setName(name);
        this.setLink(link);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
}
