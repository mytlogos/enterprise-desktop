package com.mytlogos.enterprisedesktop.worker;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.ClientModelPersister;
import com.mytlogos.enterprisedesktop.background.ReloadStat;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.SimpleToc;
import com.mytlogos.enterprisedesktop.background.api.Client;
import com.mytlogos.enterprisedesktop.background.api.NotConnectedException;
import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.model.Toc;
import com.mytlogos.enterprisedesktop.preferences.MainPreferences;
import com.mytlogos.enterprisedesktop.tools.Log;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.concurrent.Task;
import retrofit2.Response;

import java.io.IOException;
import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
public class SynchronizeTask extends Task<Void> {
    private int mediaAddedOrUpdated = 0;
    private int partAddedOrUpdated = 0;
    private int episodesAddedOrUpdated = 0;
    private int releasesAddedOrUpdated = 0;
    private int listsAddedOrUpdated = 0;
    private int externalUserAddedOrUpdated = 0;
    private int externalListAddedOrUpdated = 0;
    private int newsAddedOrUpdated = 0;
    private int mediaInWaitAddedOrUpdated = 0;
    private int totalAddedOrUpdated = 0;
    private int tocsAddedOrUpdated = 0;

    @Override
    protected Void call() throws Exception {
        Thread.currentThread().setName("Synchronize-Thread");
        final Repository repository = ApplicationConfig.getLiveDataRepository().firstElement().get();

        if (!repository.isClientOnline()) {
            cleanUp();
            Log.info("Aborting Synchronize, Client is offline");
            return null;
        }
        if (!repository.isClientAuthenticated()) {
            cleanUp();
            throw new IllegalStateException("Not Authenticated");
        }

        String contentText = "Missing title";
        try {
            this.updateTitle("Start Synchronizing");
            syncWithTime(repository);
            contentText = "Synchronization complete";
        } catch (NotConnectedException e) {
            contentText = "Not connected with Server";
            this.totalAddedOrUpdated = 0;
            throw e;
        } catch (ServerException e) {
            contentText = "Response with Error Message";
            this.totalAddedOrUpdated = 0;
            throw e;
        } catch (IOException e) {
            contentText = "Error between App and Server";
            this.totalAddedOrUpdated = 0;
            throw e;
        } catch (Exception e) {
            contentText = "Local Error";
            this.totalAddedOrUpdated = 0;
            throw e;
        } finally {
            StringBuilder builder = new StringBuilder("Added or Updated:\n");
            append(builder, "Media: ", this.mediaAddedOrUpdated);
            append(builder, "Tocs: ", this.tocsAddedOrUpdated);
            append(builder, "Parts: ", this.partAddedOrUpdated);
            append(builder, "Episodes: ", this.episodesAddedOrUpdated);
            append(builder, "Releases: ", this.releasesAddedOrUpdated);
            append(builder, "MediaLists: ", this.listsAddedOrUpdated);
            append(builder, "ExternalUser: ", this.externalUserAddedOrUpdated);
            append(builder, "ExternalLists: ", this.externalListAddedOrUpdated);
            append(builder, "News: ", this.newsAddedOrUpdated);
            append(builder, "MediaInWait: ", this.mediaInWaitAddedOrUpdated);
            notifyFinished(contentText, builder.toString(), this.totalAddedOrUpdated);

            cleanUp();
        }
        return null;
    }

    private boolean syncWithTime(Repository repository) throws IOException {
        Client client = repository.getClient(this);
        ClientModelPersister persister = repository.getPersister(this);

        LocalDateTime lastSync = MainPreferences.getLastSync();
        syncChanged(lastSync, client, persister, repository);
        MainPreferences.setLastSync(LocalDateTime.now());
        syncDeleted(client, persister, repository);
        return false;
    }

    private void notifyFinished(String title, String contentText, int finished) {
        this.notify(title, contentText, finished, finished);
    }

    private void syncChanged(LocalDateTime lastSync, Client client, ClientModelPersister persister, Repository repository) throws IOException {
        notify("Requesting New Data", null, true, true);
        Response<ClientChangedEntities> changedEntitiesResponse = client.getNew(lastSync);
        ClientChangedEntities changedEntities = Utils.checkAndGetBody(changedEntitiesResponse);

        int mediaSize = this.mediaAddedOrUpdated = changedEntities.media.size();
        int partsSize = this.partAddedOrUpdated = changedEntities.parts.size();
        int episodesSize = this.episodesAddedOrUpdated = changedEntities.episodes.size();
        int releasesSize = this.releasesAddedOrUpdated = changedEntities.releases.size();
        int listsSize = this.listsAddedOrUpdated = changedEntities.lists.size();
        int extListSize = this.externalListAddedOrUpdated = changedEntities.extLists.size();
        int extUserSize = this.externalUserAddedOrUpdated = changedEntities.extUser.size();
        int newsSize = this.newsAddedOrUpdated = changedEntities.news.size();
        int mediaInWaitSize = this.mediaInWaitAddedOrUpdated = changedEntities.mediaInWait.size();
        int tocsSize = this.tocsAddedOrUpdated = changedEntities.tocs.size();

        int total = this.totalAddedOrUpdated = mediaSize + partsSize + episodesSize + releasesSize + listsSize
                + extListSize + extUserSize + newsSize + mediaInWaitSize + tocsSize;

        StringBuilder builder = new StringBuilder();
        append(builder, "Media: ", mediaSize);
        append(builder, "Tocs: ", tocsSize);
        append(builder, "Parts: ", partsSize);
        append(builder, "Episodes: ", episodesSize);
        append(builder, "Releases: ", releasesSize);
        append(builder, "MediaLists: ", listsSize);
        append(builder, "ExternalUser: ", extUserSize);
        append(builder, "ExternalLists: ", extListSize);
        append(builder, "News: ", newsSize);
        append(builder, "MediaInWait: ", mediaInWaitSize);
        int current = 0;
        this.notify("Received New Data", builder.toString(), current, total);

        this.notify("Persisting Media", null, current, total);
        // persist all new or updated entities, media to releases needs to be in this order
        persister.persistSimpleMedia(changedEntities.media);
        current += mediaSize;
        changedEntities.media.clear();

        this.notify("Persisting Tocs", null, current, total);
        // persist all new or updated entities, media to releases needs to be in this order
        persister.persistFullTocs(changedEntities.tocs);
        current += tocsSize;
        changedEntities.tocs.clear();

        this.notify("Persisting Parts", null, current, total);
        persistParts(changedEntities.parts, client, persister, repository);
        current += partsSize;
        changedEntities.parts.clear();

        this.notify("Persisting Episodes", null, current, total);
        persistEpisodes(changedEntities.episodes, client, persister, repository);
        current += episodesSize;
        changedEntities.episodes.clear();

        this.notify("Persisting Releases", null, current, total);
        persistReleases(changedEntities.releases, client, persister, repository);
        current += releasesSize;
        changedEntities.releases.clear();

        this.notify("Persisting Lists", null, current, total);
        persister.persistUserLists(changedEntities.lists);
        current += listsSize;
        changedEntities.lists.clear();

        this.notify("Persisting ExternalUser", null, current, total);
        persister.persistExternalUsers(changedEntities.extUser);
        current += extUserSize;
        changedEntities.extUser.clear();

        this.notify("Persisting External Lists", null, current, total);
        persistExternalLists(changedEntities.extLists, client, persister, repository);
        current += extListSize;
        changedEntities.extLists.clear();

        this.notify("Persisting unused Media", null, current, total);
        persister.persistMediaInWait(changedEntities.mediaInWait);
        current += mediaInWaitSize;
        changedEntities.media.clear();

        this.notify("Persisting News", null, current, total);
        persister.persistNews(changedEntities.news);
        current += newsSize;
        changedEntities.news.clear();
        this.notify("Saved all Changes", null, current, total);
    }

    private void syncDeleted(Client client, ClientModelPersister persister, Repository repository) throws IOException {
        notify("Synchronize Deleted Items", null, true, false);
        ClientStat statBody = Utils.checkAndGetBody(client.getStats());

        ClientStat.ParsedStat parsedStat = statBody.parse();
        persister.persist(parsedStat);

        ReloadStat reloadStat = repository.checkReload(parsedStat);

        if (!reloadStat.loadMedium.isEmpty()) {
            final List<ClientMedium> media = Utils.checkAndGetBody(client.getMedia(reloadStat.loadMedium));
            persister.persistMedia(media);

            reloadStat = repository.checkReload(parsedStat);
        }

        if (!reloadStat.loadExUser.isEmpty()) {
            final List<ClientExternalUser> users = Utils.checkAndGetBody(client.getExternalUser(reloadStat.loadExUser));
            persister.persistExternalUsers(users);

            reloadStat = repository.checkReload(parsedStat);
        }

        if (!reloadStat.loadLists.isEmpty()) {
            final ClientMultiListQuery listQuery = Utils.checkAndGetBody(client.getLists(reloadStat.loadLists));
            persister.persist(listQuery);

            reloadStat = repository.checkReload(parsedStat);
        }

        if (!reloadStat.loadPart.isEmpty()) {
            Utils.doPartitionedRethrow(reloadStat.loadPart, partIds -> {
                final List<ClientPart> parts = Utils.checkAndGetBody(client.getParts(partIds));
                persister.persistParts(parts);
                return false;
            });
            reloadStat = repository.checkReload(parsedStat);
        }

        if (!reloadStat.loadPartEpisodes.isEmpty()) {
            Map<String, List<Integer>> partStringEpisodes = Utils.checkAndGetBody(client.getPartEpisodes(reloadStat.loadPartEpisodes));

            Map<Integer, List<Integer>> partEpisodes = mapStringToInt(partStringEpisodes);

            Collection<Integer> missingEpisodes = new HashSet<>();

            for (Map.Entry<Integer, List<Integer>> entry : partEpisodes.entrySet()) {
                for (Integer episodeId : entry.getValue()) {
                    if (!repository.isEpisodeLoaded(episodeId)) {
                        missingEpisodes.add(episodeId);
                    }
                }
            }
            if (!missingEpisodes.isEmpty()) {
                List<ClientEpisode> episodes = Utils.checkAndGetBody(client.getEpisodes(missingEpisodes));
                persistEpisodes(episodes, client, persister, repository);
            }
            Utils.doPartitionedRethrow(partEpisodes.keySet(), ids -> {

                Map<Integer, List<Integer>> currentEpisodes = new HashMap<>();
                for (Integer id : ids) {
                    currentEpisodes.put(id, partEpisodes.get(id));
                }
                persister.deleteLeftoverEpisodes(currentEpisodes);
                return false;
            });

            reloadStat = repository.checkReload(parsedStat);
        }


        if (!reloadStat.loadPartReleases.isEmpty()) {
            Response<Map<String, List<ClientSimpleRelease>>> partReleasesResponse = client.getPartReleases(reloadStat.loadPartReleases);

            Map<String, List<ClientSimpleRelease>> partStringReleases = Utils.checkAndGetBody(partReleasesResponse);

            Map<Integer, List<ClientSimpleRelease>> partReleases = mapStringToInt(partStringReleases);

            Collection<Integer> missingEpisodes = new HashSet<>();

            for (Map.Entry<Integer, List<ClientSimpleRelease>> entry : partReleases.entrySet()) {
                for (ClientSimpleRelease release : entry.getValue()) {
                    if (!repository.isEpisodeLoaded(release.episodeId)) {
                        missingEpisodes.add(release.episodeId);
                    }
                }
            }
            if (!missingEpisodes.isEmpty()) {
                List<ClientEpisode> episodes = Utils.checkAndGetBody(client.getEpisodes(missingEpisodes));
                persistEpisodes(episodes, client, persister, repository);
            }
            Collection<Integer> episodesToLoad = persister.deleteLeftoverReleases(partReleases);

            if (!episodesToLoad.isEmpty()) {
                Utils.doPartitionedRethrow(episodesToLoad, ids -> {
                    List<ClientEpisode> episodes = Utils.checkAndGetBody(client.getEpisodes(ids));
                    persistEpisodes(episodes, client, persister, repository);
                    return false;
                });
            }

            reloadStat = repository.checkReload(parsedStat);
        }

        if (!reloadStat.loadMediumTocs.isEmpty()) {
            final Response<List<ClientFullMediumToc>> mediumTocsResponse = client.getMediumTocs(reloadStat.loadMediumTocs);
            final List<ClientFullMediumToc> mediumTocs = Utils.checkAndGetBody(mediumTocsResponse);
            Map<Integer, List<String>> mediaTocs = new HashMap<>();

            for (ClientFullMediumToc mediumToc : mediumTocs) {
                mediaTocs.computeIfAbsent(mediumToc.mediumId, id -> new ArrayList<>()).add(mediumToc.link);
            }
            Utils.doPartitionedRethrow(mediaTocs.keySet(), mediaIds-> {
                Map<Integer, List<String>> partitionedMediaTocs = new HashMap<>();

                for (Integer mediaId : mediaIds) {
                    partitionedMediaTocs.put(mediaId, mediaTocs.get(mediaId));
                }
                this.persistTocs(partitionedMediaTocs, persister);
                persister.deleteLeftoverTocs(partitionedMediaTocs);
                return false;
            });
        }

        // as even now some errors crop up, just load all this shit and dump it in 100er steps
        if (!reloadStat.loadPartEpisodes.isEmpty() || !reloadStat.loadPartReleases.isEmpty()) {
            Collection<Integer> partsToLoad = new HashSet<>();
            partsToLoad.addAll(reloadStat.loadPartEpisodes);
            partsToLoad.addAll(reloadStat.loadPartReleases);

            Utils.doPartitionedRethrow(partsToLoad, ids -> {
                List<ClientPart> parts = Utils.checkAndGetBody(client.getParts(ids));

                persistParts(parts, client, persister, repository);
                return false;
            });
            reloadStat = repository.checkReload(parsedStat);
        }

        if (!reloadStat.loadPartEpisodes.isEmpty()) {
            String msg = String.format(
                    "Episodes of %d Parts to load even after running once",
                    reloadStat.loadPartEpisodes.size()
            );
            System.out.println(msg);
        }

        if (!reloadStat.loadPartReleases.isEmpty()) {
            String msg = String.format(
                    "Releases of %d Parts to load even after running once",
                    reloadStat.loadPartReleases.size()
            );
            System.out.println(msg);
        }
    }

    private void persistTocs(Map<Integer, List<String>> mediaTocs, ClientModelPersister persister) {
        List<Toc> inserts = new LinkedList<>();

        for (Map.Entry<Integer, List<String>> entry : mediaTocs.entrySet()) {
            final int mediumId = entry.getKey();

            for (String tocLink : entry.getValue()) {
                inserts.add(new SimpleToc(mediumId, tocLink));
            }
        }
        persister.persistTocs(inserts);
    }

    private void notify(String title, String contentText, int current, int total) {
        if (title != null) {
            this.updateTitle(title);
        }
        if (contentText != null) {
            this.updateMessage(contentText);
        }
        this.updateProgress(current, total);
    }

    private void notify(String title, String contentText, boolean indeterminate, boolean changeContent) {
        if (title != null) {
            this.updateTitle(title);
        }
        if (contentText != null) {
            this.updateMessage(contentText);
        }
        if (indeterminate) {
            this.updateProgress(-1, -1);
        } else {
            this.updateProgress(0, 0);
        }
    }

    private void cleanUp() {
//        Repository repository = ApplicationConfig.getRepository();
//        repository.syncProgress();
    }

    private void append(StringBuilder builder, String prefix, int i) {
        if (i > 0) {
            builder.append(prefix).append(i).append("\n");
        }
    }

    private void persistParts(Collection<ClientPart> parts, Client client, ClientModelPersister persister, Repository repository) throws IOException {
        Collection<Integer> missingIds = new HashSet<>();
        Collection<ClientPart> loadingParts = new HashSet<>();

        parts.removeIf(part -> {
            if (!repository.isMediumLoaded(part.getMediumId())) {
                missingIds.add(part.getMediumId());
                loadingParts.add(part);
                return true;
            }
            return false;
        });

        persister.persistParts(parts);
        if (missingIds.isEmpty()) {
            return;
        }
        Utils.doPartitionedRethrow(missingIds, ids -> {
            List<ClientMedium> parents = Utils.checkAndGetBody(client.getMedia(ids));
            persister.persistMedia(parents);
            return false;
        });
        persister.persistParts(loadingParts);
    }

    private void persistEpisodes(Collection<ClientEpisode> episodes, Client client, ClientModelPersister persister, Repository repository) throws IOException {
        Collection<Integer> missingIds = new HashSet<>();
        Collection<ClientEpisode> loading = new HashSet<>();

        episodes.removeIf(value -> {
            if (!repository.isPartLoaded(value.getPartId())) {
                missingIds.add(value.getPartId());
                loading.add(value);
                return true;
            }
            return false;
        });

        persister.persistEpisodes(episodes);
        if (missingIds.isEmpty()) {
            return;
        }
        Utils.doPartitionedRethrow(missingIds, ids -> {
            List<ClientPart> parents = Utils.checkAndGetBody(client.getParts(ids));
            this.persistParts(parents, client, persister, repository);
            return false;
        });
        persister.persistEpisodes(loading);
    }

    private void persistReleases(List<ClientPureDisplayRelease> releases, Client client, ClientModelPersister persister, Repository repository) throws IOException {
        Collection<Integer> missingIds = new HashSet<>();
        Collection<ClientPureDisplayRelease> loading = new HashSet<>();

        releases.removeIf(value -> {
            if (!repository.isEpisodeLoaded(value.getEpisodeId())) {
                missingIds.add(value.getEpisodeId());
                loading.add(value);
                return true;
            }
            return false;
        });

        persister.persistReleases(releases);
        if (missingIds.isEmpty()) {
            return;
        }
        Utils.doPartitionedRethrow(missingIds, ids -> {
            List<ClientEpisode> parents = Utils.checkAndGetBody(client.getEpisodes(ids));
            this.persistEpisodes(parents, client, persister, repository);
            return false;
        });
        persister.persistReleases(loading);
    }

    private void persistExternalLists(Collection<ClientExternalMediaList> externalMediaLists, Client client, ClientModelPersister persister, Repository repository) throws IOException {
        Collection<String> missingIds = new HashSet<>();
        Collection<ClientExternalMediaList> loading = new HashSet<>();

        externalMediaLists.removeIf(value -> {
            if (!repository.isExternalUserLoaded(value.getUuid())) {
                missingIds.add(value.getUuid());
                loading.add(value);
                return true;
            }
            return false;
        });

        persister.persistExternalMediaLists(externalMediaLists);
        if (missingIds.isEmpty()) {
            return;
        }
        Utils.doPartitionedRethrow(missingIds, ids -> {
            List<ClientExternalUser> parents = Utils.checkAndGetBody(client.getExternalUser(ids));
            persister.persistExternalUsers(parents);
            return false;
        });
        persister.persistExternalMediaLists(loading);
    }

    private <T> Map<Integer, T> mapStringToInt(Map<String, T> map) {

        Map<Integer, T> result = new HashMap<>();

        for (Map.Entry<String, T> entry : map.entrySet()) {
            result.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return result;
    }
}
