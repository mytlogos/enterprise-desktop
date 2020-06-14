package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.*;

import java.util.List;
import java.util.Map;

import com.mytlogos.enterprisedesktop.model.SearchResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

interface UserApi {

    @POST("{start}/logout")
    Call<Boolean> logout(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @PUT
    Call<Boolean> updateUser(@Url String url, @Body Map<String, Object> body);

    @GET
    Call<ClientUser> getUser(@Url String url, @QueryMap Map<String, Object> body);

    @GET("{start}/news")
    Call<List<ClientNews>> getNews(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/lists")
    Call<List<ClientMediaList>> getLists(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/invalidated")
    Call<List<InvalidatedData>> getInvalidated(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/search")
    Call<List<SearchResponse>> search(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/download")
    Call<List<ClientDownloadedEpisode>> downloadEpisodes(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/stats")
    Call<ClientStat> getStats(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/new")
    Call<ClientChangedEntities> getNew(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/toc")
    Call<List<ClientToc>> getToc(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @POST("{start}/toc")
    Call<List<ClientToc>> addToc(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    // TODO: 22.07.2019 add toc {uuid, toc: string, mediumId} ?
}
