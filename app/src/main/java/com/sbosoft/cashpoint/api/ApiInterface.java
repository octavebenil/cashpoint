package com.sbosoft.cashpoint.api;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(
        @Part MultipartBody.Part file,
        @Part("type") String type
    );

    @POST("upload/text")
    Call<ResponseBody> uploadText(
        @Body String text
    );
}