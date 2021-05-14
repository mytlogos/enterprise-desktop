package com.mytlogos.enterprisedesktop.background.api;

import com.mytlogos.enterprisedesktop.background.api.model.ClientListQuery;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMediaList;
import com.mytlogos.enterprisedesktop.background.api.model.ClientMultiListQuery;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

interface ListApi {

    @GET("{start}/all")
    Call<List<ClientMediaList>> getAll(@Path(encoded = true, value = "start") String url, @QueryMap Map<String, Object> body);

    @GET
    Call<ClientListQuery> getList(@Url String url, @QueryMap Map<String, Object> body);

    @GET
    Call<ClientMultiListQuery> getLists(@Url String url, @QueryMap Map<String, Object> body);

    @POST
    Call<ClientMediaList> addList(@Url String url, @Body Map<String, Object> body);
    
    @PUT
    Call<Boolean> updateList(@Url String url, @Body Map<String, Object> body);

    @DELETE
    Call<Boolean> deleteList(@Url String url, @Body Map<String, Object> body);
}
