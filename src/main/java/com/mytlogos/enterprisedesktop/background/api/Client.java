package com.mytlogos.enterprisedesktop.background.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.model.SearchRequest;
import com.mytlogos.enterprisedesktop.model.SearchResponse;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client {
    private static Map<Class<?>, Retrofit> retrofitMap = new HashMap<>();
    private static Map<Class<?>, String> fullClassPathMap = new HashMap<>();

    static {
        buildPathMap();
    }

    private final NetworkIdentificator identificator;
    private Authentication authentication;
    private Server server;
    private String lastNetworkSSID;
    private LocalDateTime disconnectedSince;
    private Set<DisconnectedListener> disconnectedListeners = Collections.synchronizedSet(new HashSet<>());

    public Client(NetworkIdentificator identificator) {
        this.identificator = identificator;
    }

    private static void buildPathMap() {
        Map<Class<?>, Class<?>> parentClassMap = new HashMap<>();
        Map<Class<?>, String> classPathMap = new HashMap<>();

        // set up the path pieces between each api
        classPathMap.put(BasicApi.class, "api");
        classPathMap.put(UserApi.class, "user");
        classPathMap.put(ExternalUserApi.class, "externalUser");
        classPathMap.put(ListApi.class, "list");
        classPathMap.put(ListMediaApi.class, "medium");
        classPathMap.put(MediumApi.class, "medium");
        classPathMap.put(PartApi.class, "part");
        classPathMap.put(EpisodeApi.class, "episode");
        classPathMap.put(ProgressApi.class, "progress");

        parentClassMap.put(UserApi.class, BasicApi.class);
        parentClassMap.put(ExternalUserApi.class, UserApi.class);
        parentClassMap.put(ListApi.class, UserApi.class);
        parentClassMap.put(ListMediaApi.class, ListApi.class);
        parentClassMap.put(MediumApi.class, UserApi.class);
        parentClassMap.put(PartApi.class, MediumApi.class);
        parentClassMap.put(EpisodeApi.class, PartApi.class);
        parentClassMap.put(ProgressApi.class, MediumApi.class);

        for (Class<?> apiClass : classPathMap.keySet()) {
            StringBuilder builder = new StringBuilder();

            for (Class<?> parent = apiClass; parent != null; parent = parentClassMap.get(parent)) {
                String pathPiece = classPathMap.get(parent);

                if (parent != apiClass) {
                    builder.insert(0, "/");
                }

                if (pathPiece == null) {
                    String canonicalName = apiClass.getCanonicalName();
                    throw new IllegalStateException("Api has no path piece: " + canonicalName);
                }
                builder.insert(0, pathPiece);
            }

            fullClassPathMap.put(apiClass, builder.toString());
        }
    }

    public static void main(String[] args) {
        try {
//            final URLConnection connection = URI.create("http://localhost:3000/api/user/stats?session=c604a272-4925-4dfa-8d81-802b3d4ce7d7&uuid=3a343e90-00af-11e9-972c-9be323ae4020").toURL().openConnection();
//            connection.connect();
//            Scanner s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
//            String result = s.hasNext() ? s.next() : "";
//            Gson gson = new Gson();
//            System.out.println(gson.fromJson(result, Map.class));
            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(LocalDateTime.class, new GsonAdapter.LocalDateTimeAdapter())
                    .create();
            final UserApi userApi = new Retrofit.Builder()
                    .baseUrl("http://localhost:3000/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build().create(UserApi.class);
            Map<String, Object> body = new HashMap<>();

            body.put("session", "c604a272-4925-4dfa-8d81-802b3d4ce7d7");
            body.put("uuid", "3a343e90-00af-11e9-972c-9be323ae4020");
            body.put("date", LocalDateTime.now().minusWeeks(1).toString());
            final Call<ClientChangedEntities> stats = userApi.getNew("api/user", body);
            System.out.println(stats.request().url().toString());
            final Response<ClientChangedEntities> response = stats.execute();
            System.out.println(gson.toJson(response.body()));
//            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAuthentication(String uuid, String session) {
        if (uuid == null || uuid.isEmpty() || session == null || session.isEmpty()) {
            return;
        }
        this.authentication = new Authentication(uuid, session);
    }

    public boolean isAuthenticated() {
        return this.authentication != null;
    }

    public void clearAuthentication() {
        this.authentication = null;
    }

    public void addDisconnectedListener(DisconnectedListener listener) {
        this.disconnectedListeners.add(listener);
    }

    public void removeDisconnectedListener(DisconnectedListener listener) {
        this.disconnectedListeners.remove(listener);
    }

    public Response<ClientSimpleUser> checkLogin() throws IOException {
        return this.query(BasicApi.class, BasicApi::checkLogin);
    }

    private <T, R> Response<R> query(Class<T> api, BuildCall<T, Call<R>> buildCall) throws IOException {
        try {
            Response<R> result = build(api, buildCall).execute();
            this.setConnected();
            System.out.println("Queried: " + result.raw().request().url().toString());
            return result;
        } catch (NotConnectedException e) {
            this.setDisconnected();
            throw new NotConnectedException(e);
        }
    }

    private <T, R> Call<R> build(Class<T> api, BuildCall<T, Call<R>> buildCall) throws IOException {
        Retrofit retrofit = Client.retrofitMap.get(api);
        String path = Client.fullClassPathMap.get(api);

        if (path == null) {
            throw new IllegalArgumentException("Unknown api class: " + api.getCanonicalName());
        }

        this.server = this.getServer();

        // FIXME: 29.07.2019 sometimes does not find server even though it is online
        if (this.server == null) {
            throw new NotConnectedException("No Server in reach");
        }

        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(LocalDateTime.class, new GsonAdapter.LocalDateTimeAdapter())
                    .create();
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(this.server.getAddress())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            Client.retrofitMap.put(api, retrofit);
        }

        T apiImpl = retrofit.create(api);
        return buildCall.call(apiImpl, path);
    }

    private void setConnected() {
        System.out.println("connected");
        if (this.disconnectedSince != null) {
            for (DisconnectedListener listener : this.disconnectedListeners) {
                listener.handle(this.disconnectedSince);
            }
            this.disconnectedSince = null;
        }
    }

    private void setDisconnected() {
        if (this.disconnectedSince == null) {
            System.out.println("disconnected");
            this.disconnectedSince = LocalDateTime.now();
        }
    }

    private synchronized Server getServer() throws NotConnectedException {
        String ssid = this.identificator.getSSID();

        if (ssid.isEmpty()) {
            throw new NotConnectedException("Not connected to any network");
        }
        ServerDiscovery discovery = new ServerDiscovery();

        if (ssid.equals(this.lastNetworkSSID)) {
            if (this.server == null) {
                return discovery.discover(this.identificator.getBroadcastAddress());
            } else if (this.server.isReachable()) {
                return this.server;
            }
        } else {
            this.lastNetworkSSID = ssid;
        }
        return discovery.discover(this.identificator.getBroadcastAddress());
    }

    public Response<ClientStat> getStats() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.getStats(url, body));
    }

    private Map<String, Object> userAuthenticationMap() {
        Map<String, Object> body = new HashMap<>();

        if (this.authentication == null) {
            throw new IllegalStateException("user not authenticated");
        }

        body.put("uuid", this.authentication.getUuid());
        body.put("session", this.authentication.getSession());
        return body;
    }

    public Response<Map<String, List<Integer>>> getPartEpisodes(Collection<Integer> partIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("part", partIds);
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.getPartItems(url, body));
    }

    public Response<Map<String, List<ClientSimpleRelease>>> getPartReleases(Collection<Integer> partIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("part", partIds);
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.getPartReleases(url, body));
    }

    public Response<ClientChangedEntities> getNew(LocalDateTime lastSync) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("date", lastSync);
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.getNew(url, body));
    }

    public Response<List<ClientMedium>> getAllMediaFull() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.getAll(url, body));
    }

    public Response<List<ClientPart>> getAllParts() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.getAll(url, body));
    }

    public Response<List<ClientEpisode>> getAllEpisodes() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(EpisodeApi.class, (apiImpl, url) -> apiImpl.getAll(url, body));
    }

    public Response<List<ClientRelease>> getAllReleases() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(EpisodeApi.class, (apiImpl, url) -> apiImpl.getAllReleases(url, body));
    }

    public Response<List<ClientMediaList>> getAllLists() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(ListApi.class, (apiImpl, url) -> apiImpl.getAll(url, body));
    }

    public Response<List<ClientExternalUser>> getAllExternalUsers() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(ExternalUserApi.class, (apiImpl, url) -> apiImpl.getAll(url, body));
    }

    public Response<ClientUser> getUser() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.getUser(url, body));
    }

    public Response<List<SearchResponse>> searchRequest(SearchRequest searchRequest) throws IOException {
        final Map<String, Object> map = this.userAuthenticationMap();
        map.put("text", searchRequest.title);
        map.put("medium", searchRequest.mediumType);
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.search(url, map));
    }

    public Response<ClientUser> login(String mailName, String password) throws IOException {
        return this.query(BasicApi.class, (apiImpl, url) ->
                apiImpl.login(
                        url,
                        this.userVerificationMap(mailName, password)
                ));
    }

    private Map<String, Object> userVerificationMap(String mailName, String password) {
        Map<String, Object> body = new HashMap<>();

        body.put("userName", mailName);
        body.put("pw", password);
        return body;
    }

    public Response<ClientUser> register(String mailName, String password) throws IOException {
        return this.query(BasicApi.class, (apiImpl, url) ->
                apiImpl.register(
                        url,
                        this.userVerificationMap(mailName, password)
                ));
    }

    public Response<Boolean> logout() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.logout(url, body));
    }

    public Response<Boolean> updateUser(ClientUpdateUser updateUser) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("user", updateUser);
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.updateUser(url, body));
    }

    public Response<List<ClientNews>> getNews(LocalDateTime from, LocalDateTime to) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        if (from != null) {
            body.put("from", from);
        }
        if (to != null) {
            body.put("to", to);
        }
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.getNews(url, body));
    }

    public Response<List<ClientNews>> getNews(Collection<Integer> newsIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        if (newsIds != null) {
            body.put("newsId", newsIds);
        }
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.getNews(url, body));
    }

    public Response<List<ClientMediaList>> getLists() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.getLists(url, body));
    }

    public Response<List<InvalidatedData>> getInvalidated() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.getInvalidated(url, body));
    }

    public Response<List<ClientDownloadedEpisode>> downloadEpisodes(Collection<Integer> episodeIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episode", episodeIds);
        return this.query(UserApi.class, (apiImpl, url) -> apiImpl.downloadEpisodes(url, body));
    }

    public Response<ClientExternalUser> getExternalUser(String externalUuid) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("externalUuid", externalUuid);
        return this.query(ExternalUserApi.class, (apiImpl, url) -> apiImpl.getExternalUser(url, body));
    }

    public Response<List<ClientExternalUser>> getExternalUser(Collection<String> externalUuid) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("externalUuid", externalUuid);
        return this.query(ExternalUserApi.class, (apiImpl, url) -> apiImpl.getExternalUsers(url, body));
    }

    public Response<ClientExternalUser> addExternalUser(AddClientExternalUser externalUser) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("externalUser", externalUser);
        return this.query(ExternalUserApi.class, (apiImpl, url) -> apiImpl.addExternalUser(url, body));
    }

    public Response<Boolean> deleteExternalUser(String externalUuid) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("externalUuid", externalUuid);
        return this.query(ExternalUserApi.class, (apiImpl, url) -> apiImpl.deleteExternalUser(url, body));
    }

    public Response<ClientListQuery> getList(int listId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("listId", listId);
        return this.query(ListApi.class, (apiImpl, url) -> apiImpl.getList(url, body));
    }

    public Response<ClientMultiListQuery> getLists(Collection<Integer> listIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("listId", listIds);
        return this.query(ListApi.class, (apiImpl, url) -> apiImpl.getLists(url, body));
    }

    public Response<ClientMediaList> addList(ClientMediaList mediaList) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("list", mediaList);
        return this.query(ListApi.class, (apiImpl, url) -> apiImpl.addList(url, body));
    }

    public Response<Boolean> deleteList(int listId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("listId", listId);
        return this.query(ListApi.class, (apiImpl, url) -> apiImpl.deleteList(url, body));
    }

    public Response<Boolean> updateList(ClientMediaList mediaList) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("list", mediaList);
        return this.query(ListApi.class, (apiImpl, url) -> apiImpl.updateList(url, body));
    }

    public Response<List<ClientMedium>> getListMedia(Collection<Integer> loadedMedia, int listId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("media", loadedMedia);
        body.put("listId", listId);
        return this.query(ListMediaApi.class, (apiImpl, url) -> apiImpl.getListMedia(url, body));
    }

    public Response<Boolean> addListMedia(int listId, int mediumId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("listId", listId);
        body.put("mediumId", mediumId);
        return this.query(ListMediaApi.class, (apiImpl, url) -> apiImpl.addListMedia(url, body));
    }

    public Response<Boolean> addListMedia(int listId, Collection<Integer> mediumId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("listId", listId);
        body.put("mediumId", mediumId);
        return this.query(ListMediaApi.class, (apiImpl, url) -> apiImpl.addListMedia(url, body));
    }

    public Response<Boolean> deleteListMedia(int listId, int mediumId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("listId", listId);
        body.put("mediumId", mediumId);
        return this.query(ListMediaApi.class, (apiImpl, url) -> apiImpl.deleteListMedia(url, body));
    }

    public Response<Boolean> deleteListMedia(int listId, Collection<Integer> mediumId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("listId", listId);
        body.put("mediumId", mediumId);
        return this.query(ListMediaApi.class, (apiImpl, url) -> apiImpl.deleteListMedia(url, body));
    }

    public Response<Boolean> updateListMedia(int oldListId, int newListId, int mediumId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("oldListId", oldListId);
        body.put("newListId", newListId);
        body.put("mediumId", mediumId);
        return this.query(ListMediaApi.class, (apiImpl, url) -> apiImpl.updateListMedia(url, body));
    }

    public Response<List<ClientMedium>> getMedia(Collection<Integer> mediumIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("mediumId", mediumIds);
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.getMedia(url, body));
    }

    public Response<List<Integer>> getAllMedia() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.getAllMedia(url, body));
    }

    public Response<ClientMedium> getMedium(int mediumId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("mediumId", mediumId);
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.getMedium(url, body));
    }

    public Response<List<ClientMediumInWait>> getMediumInWait() throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.getMediumInWait(url, body));
    }

    public Response<ClientMedium> createFromMediumInWait(ClientMediumInWait main, Collection<ClientMediumInWait> others, Integer listId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("createMedium", main);
        body.put("tocsMedia", others);
        body.put("listId", listId);
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.createFromMediumInWait(url, body));
    }

    public Response<Boolean> consumeMediumInWait(int mediumId, Collection<ClientMediumInWait> others) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("mediumId", mediumId);
        body.put("tocsMedia", others);
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.consumeMediumInWait(url, body));
    }

    public Response<ClientMedium> addMedia(ClientMedium clientMedium) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("medium", clientMedium);
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.addMedia(url, body));
    }

    public Response<Boolean> updateMedia(ClientMedium medium) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("medium", medium);
        return this.query(MediumApi.class, (apiImpl, url) -> apiImpl.updateMedia(url, body));
    }

    public Response<List<ClientPart>> getParts(int mediumId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("mediumId", mediumId);
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.getPart(url, body));
    }

    public Response<List<ClientPart>> getParts(Collection<Integer> partIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("partId", partIds);
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.getPart(url, body));
    }

    public Response<ClientPart> addPart(ClientPart part) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("part", part);
        body.put("mediumId", part.getMediumId());
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.addPart(url, body));
    }

    public Response<Boolean> deletePart(int partId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("partId", partId);
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.deletePart(url, body));
    }

    public Response<Boolean> updatePart(ClientPart part) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("part", part);
        return this.query(PartApi.class, (apiImpl, url) -> apiImpl.updatePart(url, body));
    }

    public Response<ClientEpisode> getEpisode(int episodeId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeId);
        return this.query(EpisodeApi.class, (apiImpl, url) -> apiImpl.getEpisode(url, body));
    }

    public Response<List<ClientEpisode>> getEpisodes(Collection<Integer> episodeIds) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeIds);
        return this.query(EpisodeApi.class, (apiImpl, url) -> apiImpl.getEpisodes(url, body));
    }

    public Response<ClientEpisode> addEpisode(int partId, ClientEpisode episode) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("partId", partId);
        body.put("episode", episode);
        return this.query(EpisodeApi.class, (apiImpl, url) -> apiImpl.addEpisode(url, body));
    }

    public Response<Boolean> deleteEpisode(int episodeId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeId);
        return this.query(EpisodeApi.class, (apiImpl, url) -> apiImpl.deleteEpisode(url, body));
    }

    public Response<Boolean> updateEpisode(ClientEpisode episode) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episode", episode);
        return this.query(EpisodeApi.class, (apiImpl, url) -> apiImpl.updateEpisode(url, body));
    }

    public Response<Float> getProgress(int episodeId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeId);
        return this.query(ProgressApi.class, (apiImpl, url) -> apiImpl.getProgress(url, body));
    }

    public Response<Boolean> addProgress(int episodeId, float progress) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeId);
        body.put("progress", progress);
        return this.query(ProgressApi.class, (apiImpl, url) -> apiImpl.addProgress(url, body));
    }

    public Response<Boolean> addProgress(Collection<Integer> episodeId, float progress) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeId);
        body.put("progress", progress);
        return this.query(ProgressApi.class, (apiImpl, url) -> apiImpl.addProgress(url, body));
    }

    public Response<Boolean> deleteProgress(int episodeId) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeId);
        return this.query(ProgressApi.class, (apiImpl, url) -> apiImpl.deleteProgress(url, body));
    }

    public Response<Boolean> updateProgress(int episodeId, float progress) throws IOException {
        Map<String, Object> body = this.userAuthenticationMap();
        body.put("episodeId", episodeId);
        body.put("progress", progress);
        return this.query(ProgressApi.class, (apiImpl, url) -> apiImpl.updateProgress(url, body));
    }

    public boolean isOnline() {
        try {
            this.server = getServer();

            if (this.server != null) {
                this.setConnected();
                return true;
            }
        } catch (NotConnectedException ignored) {
        }
        this.setDisconnected();
        return false;
    }

    @FunctionalInterface
    public interface DisconnectedListener {
        void handle(LocalDateTime timeDisconnected);
    }

    private interface BuildCall<T, R> {
        R call(T apiImpl, String url);
    }
}