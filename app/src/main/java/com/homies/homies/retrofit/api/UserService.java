package com.homies.homies.retrofit.api;

import com.homies.homies.retrofit.model.AddUser;
import com.homies.homies.retrofit.model.ChangeAdmin;
import com.homies.homies.retrofit.model.ChangePass;
import com.homies.homies.retrofit.model.DeleteUser;
import com.homies.homies.retrofit.model.GroupRequest;
import com.homies.homies.retrofit.model.GroupResponse;
import com.homies.homies.retrofit.model.LeaveGroup;
import com.homies.homies.retrofit.model.UserData;
import com.homies.homies.retrofit.model.UserRequest;
import com.homies.homies.retrofit.model.UserResponse;

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
    Call<Void> changePassword(@Header("Authorization")String authHeader,@Body ChangePass changePass);

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

    @GET("groups/{id}")
    Call<GroupResponse> groupPhoto(@Header("Authorization")String authHeader, @Path("id") int userId);

    @POST("groups/add-user")
    Call<GroupResponse> addUserGroup(@Header("Authorization") String authHeader, @Body AddUser addUser);

    @POST("groups/delete-user")
    Call<GroupResponse> deleteUserGroup(@Header("Authorization") String authHeader,@Body DeleteUser deleteUser);

    @POST("groups/delete-user")
    Call<GroupResponse> leaveUserGroup(@Header("Authorization") String authHeader, @Body LeaveGroup leaveGroup);

    @DELETE("groups/{id}")
    Call<GroupResponse> deleteGroup(@Header("Authorization")String authHeader,@Path("id") int userId);

    @POST("groups/change-admin")
    Call<GroupResponse> changeAdmin(@Header("Authorization")String authHeader,@Body ChangeAdmin changeAdmin);

    @PUT("groups/{id}")
    Call<GroupResponse> updateInfoGroup(@Header("Authorization")String authHeader,@Path("id") int userId, @Body GroupRequest groupRequest);


}
