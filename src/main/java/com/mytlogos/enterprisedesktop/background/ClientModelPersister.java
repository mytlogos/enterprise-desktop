package com.mytlogos.enterprisedesktop.background;

import com.mytlogos.enterprisedesktop.background.api.model.ClientEpisode;
import com.mytlogos.enterprisedesktop.background.api.model.ClientExternalMediaList;
import com.mytlogos.enterprisedesktop.background.api.model.ClientExternalUser;
import com.mytlogos.enterprisedesktop.background.api.model.ClientListQuery;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediaList;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMedium;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediumInWait;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMultiListQuery;
import com.mytlogos.enterprisedesktop.background.api.model.ClientNews;
import com.mytlogos.enterprisedesktop.background.api.model.ClientPart;
import com.mytlogos.enterprisedesktop.background.api.model.ClientReadEpisode;
import com.mytlogos.enterprisedesktop.background.api.model.ClientSimpleUser;
import com.mytlogos.enterprisedesktop.background.api.model.ClientUpdateUser;
import com.mytlogos.enterprisedesktop.background.api.model.ClientUser;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorkGenerator;
import com.mytlogos.enterprisedesktop.model.ToDownload;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface ClientModelPersister {
    Collection<ClientConsumer<?>> getConsumer();

    default ClientModelPersister persist(ClientEpisode... episode) {
        return this.persistEpisodes(Arrays.asList(episode));
    }

    ClientModelPersister persistEpisodes(Collection<ClientEpisode> episode);

    default ClientModelPersister persist(ClientMediaList... mediaLists) {
        return this.persistMediaLists(Arrays.asList(mediaLists));
    }

    ClientModelPersister persist(LoadWorkGenerator.FilteredEpisodes filteredEpisodes);

    ClientModelPersister persistMediaLists(Collection<ClientMediaList> mediaLists);

    default ClientModelPersister persist(ClientExternalMediaList... externalMediaLists) {
        return this.persistExternalMediaLists(Arrays.asList(externalMediaLists));
    }

    ClientModelPersister persist(LoadWorkGenerator.FilteredMediaList filteredMediaList);

    ClientModelPersister persistExternalMediaLists(Collection<ClientExternalMediaList> externalMediaLists);

    default ClientModelPersister persist(ClientExternalUser... externalUsers) {
        return this.persistExternalUsers(Arrays.asList(externalUsers));
    }

    ClientModelPersister persist(LoadWorkGenerator.FilteredExtMediaList filteredExtMediaList);

    ClientModelPersister persistExternalUsers(Collection<ClientExternalUser> externalUsers);

    default ClientModelPersister persist(ClientMedium... media) {
        return this.persistMedia(Arrays.asList(media));
    }

    ClientModelPersister persist(LoadWorkGenerator.FilteredExternalUser filteredExternalUser);

    ClientModelPersister persistMedia(Collection<ClientMedium> media);

    default ClientModelPersister persist(ClientNews... news) {
        return this.persistNews(Arrays.asList(news));
    }

    ClientModelPersister persist(LoadWorkGenerator.FilteredMedia filteredMedia);

    ClientModelPersister persistNews(Collection<ClientNews> news);

    default ClientModelPersister persist(ClientPart... parts) {
        return this.persistParts(Arrays.asList(parts));
    }

    ClientModelPersister persistParts(Collection<ClientPart> parts);

    ClientModelPersister persist(LoadWorkGenerator.FilteredReadEpisodes filteredReadEpisodes);

    ClientModelPersister persist(ClientListQuery query);

    ClientModelPersister persist(ClientMultiListQuery query);

    ClientModelPersister persist(ClientUser user);

    ClientModelPersister persist(ClientUpdateUser user);

    ClientModelPersister persistToDownloads(Collection<ToDownload> toDownloads);

    default ClientModelPersister persist(ClientReadEpisode... readEpisodes) {
        return this.persistReadEpisodes(Arrays.asList(readEpisodes));
    }

    ClientModelPersister persist(LoadWorkGenerator.FilteredParts filteredParts);

    ClientModelPersister persistReadEpisodes(Collection<ClientReadEpisode> readMedia);

    void finish();

    ClientModelPersister persist(ToDownload toDownload);

    void persistMediaInWait(List<ClientMediumInWait> medium);

    ClientModelPersister persist(ClientSimpleUser user);
}
