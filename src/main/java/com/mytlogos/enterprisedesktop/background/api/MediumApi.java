package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.ClientEpisode;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMedium;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediumInWait;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

interface MediumApi {

    @GET
    Call<ClientMedium> getMedium(@Url String url, @QueryMap Map<String, Object> body);

    @GET
    Call<List<ClientMedium>> getMedia(@Url String url, @QueryMap Map<String, Object> body);

    @GET("{start}/allFull")
    Call<List<ClientMedium>> getAll(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    @GET("{start}/all")
    Call<List<Integer>> getAllMedia(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/unused")
    Call<List<ClientMediumInWait>> getMediumInWait(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @PUT("{start}/unused")
    Call<Boolean> consumeMediumInWait(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @POST("{start}/create")
    Call<ClientMedium> createFromMediumInWait(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @POST
    Call<ClientMedium> addMedia(@Url String url, @Body Map<String, Object> body);

    @PUT
    Call<Boolean> updateMedia(@Url String url, @Body Map<String, Object> body);
}
