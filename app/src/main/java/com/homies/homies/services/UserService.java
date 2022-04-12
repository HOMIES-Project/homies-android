package com.homies.homies.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {

    @POST("authenticate")
    Call<UserResponse> loginUser(@Body UserRequest userRequest);

    @POST("register")
    Call<Void> saveUser(@Body UserRequest userRequest);

    @POST("account/change-password")
    Call<UserResponse> changePassword(@Body UserRequest userRequest);

    @POST("account/reset-password/init")
    Call<Void> resetPassword(@Body String email);

    @GET("groups")
    Call<List<GroupResponse>> getGroup(@Header("Authorization") String authHeader);

    @POST("groups")
    Call<GroupResponse> saveGroup(@Body GroupRequest groupRequest);

    @POST("/api/groups/add-user")
    Call<Void> addUser(@Body UserRequest userRequest);

    @POST("/api/groups/delete-user")
    Call<Void> eraseUser(@Body UserAdmin userAdmin);
}
