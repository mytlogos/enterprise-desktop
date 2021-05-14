package com.mytlogos.enterprisedesktop.background.api.model;

import java.util.List;

/**
 * API Model for SecondaryMedium.
 * Enterprise Web API 1.0.2.
 */
public class ClientSecondaryMedium {
    public int id;
    public int totalEpisodes;
    public int readEpisodes;
    public List<ClientFullToc> tocs;
}
