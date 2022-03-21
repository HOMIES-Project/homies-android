package com.homies.homies;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {


    @POST("register")
    Call<Void> saveUser(@Body UserRequest userRequest);
}
