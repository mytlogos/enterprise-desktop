package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.ClientEpisode;
import com.mytlogos.enterprisedesktop.background.api.model.ClientRelease;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

interface EpisodeApi {

    @GET
    Call<ClientEpisode> getEpisode(@Url String url, @QueryMap Map<String, Object> body);

    @GET("{start}/all")
    Call<List<ClientEpisode>> getAll(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    @GET("{start}/releases/all")
    Call<List<ClientRelease>> getAllReleases(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    @GET
    Call<List<ClientEpisode>> getEpisodes(@Url String url, @QueryMap Map<String, Object> body);

    @POST
    Call<ClientEpisode> addEpisode(@Url String url, @Body Map<String, Object> body);

    @DELETE
    Call<Boolean> deleteEpisode(@Url String url, @Body Map<String, Object> body);

    @PUT
    Call<Boolean> updateEpisode(@Url String url, @Body Map<String, Object> body);
}
