package com.mytlogos.enterprisedesktop.background;

import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorkGenerator;
import com.mytlogos.enterprisedesktop.model.ToDownload;
import com.mytlogos.enterprisedesktop.model.Toc;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ClientModelPersister {
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

    ClientModelPersister persist(ClientStat.ParsedStat parsedStat);

    void deleteLeftoverEpisodes(Map<Integer, List<Integer>> partEpisodes);

    Collection<Integer> deleteLeftoverReleases(Map<Integer, List<ClientSimpleRelease>> partReleases);

    ClientModelPersister persistReleases(Collection<ClientRelease> releases);

    void deleteLeftoverTocs(Map<Integer, List<String>> mediaTocs);

    ClientModelPersister persistTocs(Collection<? extends Toc> tocs);
}
