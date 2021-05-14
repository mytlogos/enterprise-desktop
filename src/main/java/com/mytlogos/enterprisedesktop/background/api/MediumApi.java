package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.ClientMedium;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediumInWait;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediumRelease;
import com.mytlogos.enterprisedesktop.background.api.model.ClientSecondaryMedium;
import com.mytlogos.enterprisedesktop.background.api.model.ClientSimpleMedium;

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

    @GET("{start}/unused")
    Call<List<ClientMediumInWait>> getMediumInWait(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/all")
    Call<List<Integer>> getAllMedia(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/allFull")
    Call<List<ClientSimpleMedium>> getAll(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    // TODO
    @GET("{start}/allSecondary")
    Call<List<ClientSecondaryMedium>> getAllSecondary(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    // TODO
    @GET("{start}/releases")
    Call<List<ClientMediumRelease>> getReleases(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    @PUT("{start}/unused")
    Call<Boolean> consumeMediumInWait(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @POST("{start}/create")
    Call<ClientMedium> createFromMediumInWait(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @POST("{start}/split")
    Call<Integer> splitMedium(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @POST("{start}/merge")
    Call<Boolean> mergeMedia(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);

    @POST("{start}/transfer")
    Call<Boolean> transferToc(@Path(value = "start", encoded = true) String url, @Body Map<String, Object> body);
    
    @GET
    Call<ClientMedium> getMedium(@Url String url, @QueryMap Map<String, Object> body);

    @GET
    Call<List<ClientMedium>> getMedia(@Url String url, @QueryMap Map<String, Object> body);

    @POST
    Call<ClientSimpleMedium> addMedia(@Url String url, @Body Map<String, Object> body);

    @PUT
    Call<Boolean> updateMedia(@Url String url, @Body Map<String, Object> body);
}
