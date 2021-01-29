package com.askapp.server;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/sign-up")
    Call<Void> executeSignUp(@Body HashMap<String, String> map);

    @POST("/sign-up/verify")
    Call<Void> executePhoneVerification(@Body HashMap<String, String> map);

    @POST("/update/PTK")
    Call<Void> executeUpdatePTK(@Body HashMap<String, String> map);

    @POST("/update/name")
    Call<Void> executeUpdateName(@Body HashMap<String, String> map);

    @POST("/user/check")
    Call<Void> executeCheckUser(@Body HashMap<String, String> map);



}