package com.askapp.server;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/user/signUp")
    Call<Void> executeSignUp(@Body HashMap<String, String> map);

    @POST("/user/phone/verify")
    Call<Void> executePhoneVerification(@Body HashMap<String, String> map);

}