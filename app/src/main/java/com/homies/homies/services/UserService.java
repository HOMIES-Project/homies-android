package com.homies.homies.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("authenticate")
    Call<UserResponse> loginUser(@Body UserRequest userRequest);

    @POST("register")
    Call<Void> saveUser(@Body UserRequest userRequest);

    @POST("account/reset-password/init")
    Call<UserResponse> resetUser(@Body UserRequest userRequest);

}
