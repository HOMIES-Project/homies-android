package com.homies.homies.services;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @Multipart
    @POST("user-data/{id}")
    Call <UserData> uploadPhoto(
            @Header("Authorization")String authHeader,
            @Path("id") int userId,
            @Part("descripcion") UserRequest description,
            @Part MultipartBody.Part photo);

    @DELETE("user-data/{id}")
    Call<UserResponse> deleteUser(@Header("Authorization")String authHeader,@Path("id") int userId);

    @GET("groups/{id}")
    Call<GroupResponse> groupInfo(@Header("Authorization")String authHeader,@Path("id") int userId);

    


}
