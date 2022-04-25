package com.homies.homies.retrofit.api;

import com.homies.homies.retrofit.model.group.AddUserGroupResponse;
import com.homies.homies.retrofit.model.group.DeleteUserGroupRequest;
import com.homies.homies.retrofit.model.group.DeleteUserGroupResponse;
import com.homies.homies.retrofit.model.GroupRequest;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.retrofit.model.UserRequest;
import com.homies.homies.retrofit.model.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

    @POST("groups")
    Call<GroupResponse> saveGroup(@Header("Authorization")String authHeader, @Body GroupRequest groupRequest);

    @GET("user-data/{id}")
    Call<UserData> userInfo(@Header("Authorization")String authHeader, @Path("id") int userId);

    @PUT("user-data/{id}")
    Call<UserData> updateInfo(@Header("Authorization")String authHeader,@Path("id") int userId, @Body UserRequest userRequest);

    @DELETE("user-data/{id}")
    Call<UserResponse> deleteUser(@Header("Authorization")String authHeader,@Path("id") int userId);

    @GET("groups/{id}")
    Call<GroupResponse> groupInfo(@Header("Authorization")String authHeader,@Path("id") int userId);

    @POST("groups/add-user")
    Call<ArrayList<AddUserGroupResponse>> addUserGroup(@Header("Authorization") String authHeader);

    @POST("groups/delete-user")
    Call<DeleteUserGroupResponse> deleteUser(@Header("Authorization") String authHeader, @Body DeleteUserGroupRequest deleteUserGroupRequest);



}