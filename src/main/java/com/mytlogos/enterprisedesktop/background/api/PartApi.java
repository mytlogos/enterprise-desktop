package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.ClientPart;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

interface PartApi {

    @GET
    Call<List<ClientPart>> getPart(@Url String url, @QueryMap Map<String, Object> body);

    @POST
    Call<ClientPart> addPart(@Url String url, @Body Map<String, Object> body);

    @DELETE
    Call<Boolean> deletePart(@Url String url, @Body Map<String, Object> body);

    @PUT
    Call<Boolean> updatePart(@Url String url, @Body Map<String, Object> body);
}
