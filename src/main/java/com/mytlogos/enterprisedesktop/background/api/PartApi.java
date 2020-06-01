package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.ClientPart;

import java.util.List;
import java.util.Map;

import com.mytlogos.enterprisedesktop.background.api.model.ClientSimpleRelease;
import retrofit2.Call;
import retrofit2.http.*;

interface PartApi {

    @GET
    Call<List<ClientPart>> getPart(@Url String url, @QueryMap Map<String, Object> body);

    @GET("{start}/all")
    Call<List<ClientPart>> getAll(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    @GET("{start}/items")
    Call<Map<String, List<Integer>>> getPartItems(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @GET("{start}/releases")
    Call<Map<String, List<ClientSimpleRelease>>> getPartReleases(@Path(value = "start", encoded = true) String url, @QueryMap Map<String, Object> body);

    @POST
    Call<ClientPart> addPart(@Url String url, @Body Map<String, Object> body);

    @DELETE
    Call<Boolean> deletePart(@Url String url, @Body Map<String, Object> body);

    @PUT
    Call<Boolean> updatePart(@Url String url, @Body Map<String, Object> body);
}
