package com.homies.homies.services;

import static com.squareup.okhttp.internal.Internal.instance;

import com.google.android.gms.common.api.Api;
import com.homies.homies.services.UserService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://homies-back-app.herokuapp.com/api/")
                //.baseUrl("https://homies-pruebas-jorge.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }



    public static UserService getService() {

        UserService userService = getRetrofit().create(UserService.class);
        return userService;

        
    }



}
