package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.*;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

interface UserApi {

    @POST("{start}/logout")
    Call<Boolean> logout(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @PUT
    Call<Boolean> updateUser(@Url String url, @Body Map<String, Object> body);

    @GET
    Call<ClientUser> getUser(@Url String url, @QueryMap Map<String, Object> body);

    @GET("{start}/lists")
    Call<List<ClientMediaList>> getLists(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    // post "{start}/bookmarked"

    @GET("{start}/searchtoc")
    Call<List<ClientFullToc>> searchToc(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @POST("{start}/toc")
    Call<Boolean> addToc(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @GET("{start}/toc")
    Call<List<ClientFullMediumToc>> getToc(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @DELETE("{start}/toc")
    Call<Boolean> removeToc(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/search")
    Call<List<ClientSearchResult>> search(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/stats")
    Call<ClientStat> getStats(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/new")
    Call<ClientChangedEntities> getNew(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/download")
    Call<List<ClientDownloadedEpisode>> downloadEpisodes(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    // get "{start}/events"
}
