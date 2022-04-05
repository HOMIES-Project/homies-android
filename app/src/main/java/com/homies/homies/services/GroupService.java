package com.homies.homies.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GroupService {

    @GET("groups")
    Call<UserResponse> getGroup(@Body GroupRequest groupRequest);

    @POST("groups")
    Call<Void> saveGroup(@Body GroupRequest groupRequest);
}
