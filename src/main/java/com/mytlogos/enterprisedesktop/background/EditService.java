package com.mytlogos.enterprisedesktop.background;


import com.mytlogos.enterprisedesktop.background.api.Client;
import com.mytlogos.enterprisedesktop.background.api.NotConnectedException;
import com.mytlogos.enterprisedesktop.background.api.model.ClientListQuery;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediaList;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMedium;
import com.mytlogos.enterprisedesktop.background.api.model.ClientUpdateUser;
import com.mytlogos.enterprisedesktop.model.*;
import com.mytlogos.enterprisedesktop.tools.Utils;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mytlogos.enterprisedesktop.background.EditService.Event.*;
import static com.mytlogos.enterprisedesktop.background.EditService.EditObject.*;


class EditService {
    private final Client client;
    private final DatabaseStorage storage;
    private final ClientModelPersister persister;
    private final ExecutorService pushEditExecutor = Executors.newSingleThreadExecutor(Utils.threadFactory("PushEdits-Thread"));

    EditService(Client client, DatabaseStorage storage, ClientModelPersister persister) {
        this.client = client;
        this.storage = storage;
        this.persister = persister;
        this.client.addDisconnectedListener(timeDisconnected -> this.pushEditExecutor.execute(this::publishEditEvents));
    }

    private void publishEditEvents() {
        List<? extends EditEvent> events = this.storage.getEditEvents();
        if (events.isEmpty()) {
            return;
        }
        events.sort(Comparator.comparing(EditEvent::getDateTime));
        Map<Integer, Map<Integer, List<EditEvent>>> objectTypeEventMap = new HashMap<>();

        for (EditEvent event : events) {
            objectTypeEventMap
                    .computeIfAbsent(event.getObjectType(), integer -> new HashMap<>())
                    .computeIfAbsent(event.getEventType(), integer -> new ArrayList<>())
                    .add(event);
        }
        Collection<EditEvent> consumedEvents = new ArrayList<>();

        for (Map.Entry<Integer, Map<Integer, List<EditEvent>>> entry : objectTypeEventMap.entrySet()) {
            try {
                boolean consumed = true;
                EditObject editObject = null;

                for (EditObject value : EditObject.values()) {
                    if (entry.getKey() == value.getValue()) {
                        editObject = value;
                        break;
                    }
                }
                if (editObject == null) {
                    System.err.println("unknown event object type: " + entry.getKey());
                } else {
                    switch (editObject) {
                        case USER:
                            this.publishUserEvents(entry.getValue());
                            break;
                        case EXTERNAL_USER:
                            this.publishExternalUserEvents(entry.getValue());
                            break;
                        case EXTERNAL_LIST:
                            this.publishExternalListEvents(entry.getValue());
                            break;
                        case LIST:
                            this.publishListEvents(entry.getValue());
                            break;
                        case MEDIUM:
                            this.publishMediumEvents(entry.getValue());
                            break;
                        case PART:
                            this.publishPartEvents(entry.getValue());
                            break;
                        case EPISODE:
                            this.publishEpisodeEvents(entry.getValue());
                            break;
                        case RELEASE:
                            this.publishReleaseEvents(entry.getValue());
                            break;
                        case NEWS:
                            this.publishNewsEvents(entry.getValue());
                            break;
                    }
                    for (List<EditEvent> value : entry.getValue().values()) {
                        consumedEvents.addAll(value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.storage.removeEditEvents(consumedEvents);
    }

    private void publishUserEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        System.out.println(typeEventsMap);
        // TODO: 26.09.2019 implement
    }

    private void publishExternalUserEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        System.out.println(typeEventsMap);
        // TODO: 26.09.2019 implement
    }

    private void publishExternalListEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        System.out.println(typeEventsMap);
        // TODO: 26.09.2019 implement
    }

    private void publishListEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        System.out.println(typeEventsMap);
        // TODO: 26.09.2019 implement
    }

    private void publishMediumEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        for (Map.Entry<Integer, List<EditEvent>> entry : typeEventsMap.entrySet()) {
            List<EditEvent> value = entry.getValue();

            // TODO: 26.09.2019 implement
        }
    }

    private void publishPartEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        System.out.println(typeEventsMap);
    }

    private void publishEpisodeEvents(Map<Integer, List<EditEvent>> typeEventsMap) throws NotConnectedException {
        for (Map.Entry<Integer, List<EditEvent>> entry : typeEventsMap.entrySet()) {
            List<EditEvent> value = entry.getValue();

            if (entry.getKey() == CHANGE_PROGRESS.getValue()) {
                this.publishEpisodeProgress(value);
            }
        }
        // TODO: 26.09.2019 implement
    }

    private void publishReleaseEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        System.out.println(typeEventsMap);
        // TODO: 26.09.2019 implement
    }

    private void publishNewsEvents(Map<Integer, List<EditEvent>> typeEventsMap) {
        System.out.println(typeEventsMap);
        // TODO: 26.09.2019 implement
    }

    private void publishEpisodeProgress(List<EditEvent> value) throws NotConnectedException {
        Map<Integer, EditEvent> latestProgress = new HashMap<>();
        Map<Integer, EditEvent> earliestProgress = new HashMap<>();

        for (EditEvent event : value) {
            latestProgress.merge(event.getId(), event, (editEvent, editEvent2) -> {
                if (editEvent2.getDateTime().isAfter(editEvent.getDateTime())) {
                    return editEvent2;
                } else {
                    return editEvent;
                }
            });
            earliestProgress.merge(event.getId(), event, (editEvent, editEvent2) -> {
                if (editEvent2.getDateTime().isBefore(editEvent.getDateTime())) {
                    return editEvent2;
                } else {
                    return editEvent;
                }
            });
        }
        Map<Float, Set<Integer>> currentProgressEpisodeMap = new HashMap<>();

        for (Map.Entry<Integer, EditEvent> latestEntry : latestProgress.entrySet()) {
            String newValue = latestEntry.getValue().getSecondValue();

            float newProgress = this.parseProgress(newValue);
            currentProgressEpisodeMap
                    .computeIfAbsent(newProgress, aFloat -> new HashSet<>())
                    .add(latestEntry.getKey());
        }

        for (Map.Entry<Float, Set<Integer>> progressEntry : currentProgressEpisodeMap.entrySet()) {
            Float progress = progressEntry.getKey();
            Set<Integer> ids = progressEntry.getValue();

            try {
                if (!this.updateProgressOnline(progress, ids)) {
                    Map<Float, Set<Integer>> progressMap = new HashMap<>();

                    for (Integer id : ids) {
                        EditEvent event = earliestProgress.get(id);
                        if (event == null) {
                            throw new IllegalStateException("expected a value, not null for: " + id);
                        }
                        float idProgress = this.parseProgress(event.getFirstValue());
                        progressMap.computeIfAbsent(idProgress, aFloat -> new HashSet<>()).add(id);
                    }
                    progressMap.forEach((updateProgress, progressIds) -> this.storage.updateProgress(progressIds, updateProgress));
                }
            } catch (NotConnectedException e) {
                throw new NotConnectedException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private float parseProgress(String value) {
        float progress;
        try {
            progress = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            progress = Boolean.parseBoolean(value) ? 1 : 0;
        }
        return progress;
    }

    private boolean updateProgressOnline(float progress, Collection<Integer> ids) throws IOException {
        Response<Boolean> response = this.client.addProgress(ids, progress);

        if (!response.isSuccessful() || response.body() == null || !response.body()) {
            // TODO 06.3.2020: better error handling
            return false;
        }
        this.storage.updateProgress(ids, progress);
        return true;
    }

    void updateUser(UpdateUser updateUser) {
        TaskManager.runTask(() -> {
            User value = this.storage.getUserNow();

            if (value == null) {
                throw new IllegalArgumentException("cannot change user when none is logged in");
            }
            ClientUpdateUser user = new ClientUpdateUser(
                    value.getUuid(), updateUser.getName(),
                    updateUser.getPassword(),
                    updateUser.getNewPassword()
            );
            if (!this.client.isOnline()) {
                System.err.println("offline user edits are not allowed");
                return;
            }
            try {
                Boolean body = this.client.updateUser(user).body();

                if (body != null && body) {
                    this.persister.persist(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    CompletableFuture<String> updateListMedium(MediaListSetting listSetting, int newMediumType) {
        return TaskManager.runCompletableTask(() -> {
            if (listSetting instanceof ExternalMediaListSetting) {
                return "Cannot update External Lists";
            }
            int listId = listSetting.getListId();
            ClientMediaList mediaList = new ClientMediaList(
                    listSetting.getUuid(),
                    listId,
                    listSetting.getName(),
                    newMediumType,
                    new int[0]
            );
            return this.updateList(listSetting.getName(), newMediumType, listId, mediaList);
        });
    }

    private String updateList(String newName, int newMediumType, int listId, ClientMediaList mediaList) {
        try {
            if (!this.client.isOnline()) {
                MediaListSetting setting = this.storage.getListSettingNow(listId, false);

                if (setting == null) {
                    return "Not available in storage";
                }
                List<EditEvent> editEvents = new ArrayList<>();

                if (!Objects.equals(setting.getName(), newName)) {
                    editEvents.add(new EditEventImpl(listId, MEDIUM.getValue(), CHANGE_NAME.getValue(), setting.getName(), newName));
                }
                if (setting.getMedium() != newMediumType) {
                    editEvents.add(new EditEventImpl(listId, MEDIUM.getValue(), CHANGE_TYPE.getValue(), setting.getMedium(), newMediumType));
                }
                this.storage.insertEditEvent(editEvents);
                this.persister.persist(mediaList).finish();
                return "";
            }
            this.client.updateList(mediaList);
            ClientListQuery query = this.client.getList(listId).body();
            this.persister.persist(query).finish();
        } catch (IOException e) {
            e.printStackTrace();
            return "Could not update List";
        }
        return "";
    }

    CompletableFuture<String> updateListName(MediaListSetting listSetting, String newName) {
        return TaskManager.runCompletableTask(() -> {
            if (listSetting instanceof ExternalMediaListSetting) {
                return "Cannot update External Lists";
            }
            int listId = listSetting.getListId();
            ClientMediaList mediaList = new ClientMediaList(
                    listSetting.getUuid(),
                    listId,
                    newName,
                    listSetting.getMedium(),
                    new int[0]
            );
            return updateList(newName, listSetting.getMedium(), listId, mediaList);
        });
    }

    CompletableFuture<String> updateMedium(MediumSetting mediumSettings) {
        return TaskManager.runCompletableTask(() -> {
            int mediumId = mediumSettings.getMediumId();
            ClientMedium clientMedium = new ClientMedium(
                    new int[0],
                    new int[0],
                    mediumSettings.getCurrentRead(),
                    new int[0],
                    mediumId,
                    mediumSettings.getCountryOfOrigin(),
                    mediumSettings.getLanguageOfOrigin(),
                    mediumSettings.getAuthor(),
                    mediumSettings.getTitle(),
                    mediumSettings.getMedium(),
                    mediumSettings.getArtist(),
                    mediumSettings.getLang(),
                    mediumSettings.getStateOrigin(),
                    mediumSettings.getStateTL(),
                    mediumSettings.getSeries(),
                    mediumSettings.getUniverse()
            );

            if (!this.client.isOnline()) {
                MediumSetting setting = this.storage.getMediumSettingsNow(mediumId);

                if (setting == null) {
                    return "Not available in storage";
                }
                List<EditEvent> editEvents = new ArrayList<>();

                if (!Objects.equals(setting.getTitle(), mediumSettings.getTitle())) {
                    editEvents.add(new EditEventImpl(mediumId, MEDIUM.getValue(), CHANGE_NAME.getValue(), setting.getTitle(), mediumSettings.getTitle()));
                }
                if (setting.getMedium() != mediumSettings.getMedium()) {
                    editEvents.add(new EditEventImpl(mediumId, MEDIUM.getValue(), CHANGE_TYPE.getValue(), setting.getMedium(), mediumSettings.getMedium()));
                }
                this.storage.insertEditEvent(editEvents);
                this.persister.persist(clientMedium).finish();
            }
            try {
                this.client.updateMedia(clientMedium);
                ClientMedium medium = this.client.getMedium(mediumId).body();
                this.persister.persist(medium).finish();
            } catch (IOException e) {
                e.printStackTrace();
                return "Could not update Medium";
            }
            return "";
        });
    }

    void updateRead(Collection<Integer> episodeIds, boolean read) throws Exception {
        float progress = read ? 1f : 0f;
        Utils.doPartitionedEx(episodeIds, ids -> {
            if (!this.client.isOnline()) {
                List<Integer> filteredIds = this.storage.getReadEpisodes(episodeIds, !read);

                if (filteredIds.isEmpty()) {
                    return false;
                }
                Collection<EditEvent> events = new ArrayList<>(filteredIds.size());
                for (Integer id : filteredIds) {
                    events.add(new EditEventImpl(id, EPISODE.getValue(), CHANGE_PROGRESS.getValue(), null, progress));
                }
                this.storage.insertEditEvent(events);
                this.storage.updateProgress(filteredIds, progress);
                return false;
            }
            if (!updateProgressOnline(progress, ids)) {
                return null;
            }
            return false;
        });
    }

    CompletableFuture<Boolean> removeItemFromList(int listId, Collection<Integer> mediumIds) {
        return TaskManager.runCompletableTask(() -> {
            try {
                if (!this.client.isOnline()) {
                    Collection<EditEvent> events = new ArrayList<>(mediumIds.size());

                    for (Integer id : mediumIds) {
                        EditEvent event = new EditEventImpl(id, MEDIUM.getValue(), REMOVE_FROM.getValue(), listId, null);
                        events.add(event);
                    }
                    this.storage.insertEditEvent(events);
                    this.storage.removeItemFromList(listId, mediumIds);
                    this.storage.insertDanglingMedia(mediumIds);
                    return true;
                }

                Response<Boolean> response = this.client.deleteListMedia(listId, mediumIds);
                Boolean success = response.body();

                if (success != null && success) {
                    this.storage.removeItemFromList(listId, mediumIds);
                    this.storage.insertDanglingMedia(mediumIds);
                    return true;
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    CompletableFuture<Boolean> addMediumToList(int listId, Collection<Integer> ids) {
        return TaskManager.runCompletableTask(() -> {
            try {
                // to prevent duplicates
                Collection<Integer> items = this.storage.getListItems(listId);
                ids.removeAll(items);

                // adding nothing cannot fail
                if (ids.isEmpty()) {
                    return true;
                }
                if (!this.client.isOnline()) {
                    Collection<EditEvent> events = new ArrayList<>(ids.size());

                    for (Integer id : ids) {
                        EditEvent event = new EditEventImpl(id, MEDIUM.getValue(), ADD_TO.getValue(), null, listId);
                        events.add(event);
                    }
                    this.storage.insertEditEvent(events);
                    this.storage.addItemsToList(listId, ids);
                    return true;
                }

                Response<Boolean> response = this.client.addListMedia(listId, ids);
                if (response.body() == null || !response.body()) {
                    return false;
                }
                this.storage.addItemsToList(listId, ids);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    CompletableFuture<Boolean> moveItemFromList(int oldListId, int newListId, int mediumId) {
        return TaskManager.runCompletableTask(() -> {
            try {
                if (!this.client.isOnline()) {
                    EditEvent event = new EditEventImpl(mediumId, MEDIUM.getValue(), MOVE.getValue(), oldListId, newListId);
                    this.storage.insertEditEvent(event);
                    this.storage.removeItemFromList(oldListId, mediumId);
                    this.storage.addItemsToList(newListId, Collections.singleton(mediumId));
                    return true;
                }
                Response<Boolean> response = this.client.updateListMedia(oldListId, newListId, mediumId);
                Boolean success = response.body();

                if (success != null && success) {
                    this.storage.removeItemFromList(oldListId, mediumId);
                    this.storage.addItemsToList(newListId, Collections.singleton(mediumId));
                    return true;
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    CompletableFuture<Boolean> moveMediaToList(int oldListId, int listId, Collection<Integer> ids) {
        return TaskManager.runCompletableTask(() -> {
            try {
                // to prevent duplicates
                Collection<Integer> items = this.storage.getListItems(listId);
                ids.removeAll(items);

                // adding nothing cannot fail
                if (ids.isEmpty()) {
                    return true;
                }
                if (!this.client.isOnline()) {
                    Collection<EditEvent> events = new ArrayList<>(ids.size());

                    for (Integer id : ids) {
                        EditEvent event = new EditEventImpl(id, EditObject.MEDIUM.getValue(), Event.MOVE.getValue(), oldListId, listId);
                        events.add(event);
                    }
                    this.storage.insertEditEvent(events);
                    this.storage.moveItemsToList(oldListId, listId, ids);
                    return true;
                }

                Collection<Integer> successMove = new ArrayList<>();

                for (Integer id : ids) {
                    Response<Boolean> response = this.client.updateListMedia(oldListId, listId, id);
                    Boolean success = response.body();

                    if (success != null && success) {
                        successMove.add(id);
                    }
                }
                this.storage.moveItemsToList(oldListId, listId, successMove);
                return !successMove.isEmpty();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    enum EditObject {
        USER(1),
        EXTERNAL_USER(2),
        EXTERNAL_LIST(3),
        LIST(4),
        MEDIUM(5),
        PART(6),
        EPISODE(7),
        RELEASE(8),
        NEWS(9),
        ;

        private final int value;

        EditObject(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum Event {
        ADD(1),
        REMOVE(2),
        MOVE(3),
        ADD_TO(4),
        REMOVE_FROM(5),
        MERGE(6),
        CHANGE_NAME(7),
        CHANGE_TYPE(8),
        ADD_TOC(9),
        REMOVE_TOC(10),
        CHANGE_PROGRESS(11),
        CHANGE_READ(12);

        private final int value;

        Event(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
