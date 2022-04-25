package com.homies.homies.services;

import androidx.constraintlayout.widget.Group;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("user-data/{id}")
    Call<UserData> userInfo(@Header("Authorization")String authHeader,@Path("id") int userId);

    @PUT("user-data/{id}")
    Call<UserData> updateInfo(@Header("Authorization")String authHeader,@Path("id") int userId, @Body UserRequest userRequest);

    @POST("user-data/{id}")
    Call <UserData> uploadPhoto(
            @Header("Authorization")String authHeader,
            @Path("id") int userId,
            @Field("photo") String encodedImage);

    @DELETE("user-data/{id}")
    Call<UserResponse> deleteUser(@Header("Authorization")String authHeader,@Path("id") int userId);

    @GET("groups/{id}")
    Call<GroupResponse> groupInfo(@Header("Authorization")String authHeader,@Path("id") int userId);

    @POST("groups/add-user")
    Call<ArrayList<AddUserGroupResponse>> addUserGroup(@Header("Authorization") String authHeader);

    @POST("groups/delete-user")
    Call<DeleteUserGroupResponse> deleteUser(@Header("Authorization") String authHeader, @Body DeleteUserGroupRequest deleteUserGroupRequest);



}
