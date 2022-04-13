package com.homies.homies.services;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    Call<GroupResponse> saveGroup(@Header("Authorization")String authHeader, @Body GroupRequest groupRequest);

    @DELETE("user-data/{id}")
    Call<UserResponse> deleteUser(@Header("Authorization")String authHeader,@Path("id") int userId);


}
