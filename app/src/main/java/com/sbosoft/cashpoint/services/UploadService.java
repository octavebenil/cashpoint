package com.sbosoft.cashpoint.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.File;
import java.util.concurrent.TimeUnit;
import com.sbosoft.cashpoint.api.ApiInterface;

public class UploadService extends Service {
    private static final String TAG = "UploadService";
    private static final String BASE_URL = "https://votre-api-url.com/"; // À modifier
    private ApiInterface apiInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofit();
    }

    private void initRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public void uploadFile(String filePath, String type) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(
            MediaType.parse(getMimeType(filePath)),
            file
        );

        MultipartBody.Part body = MultipartBody.Part.createFormData(
            "file",
            file.getName(),
            requestFile
        );

        apiInterface.uploadFile(body, type)
            .enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call,
                                    retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Upload réussi");
                    } else {
                        Log.e(TAG, "Erreur upload: " + response.code());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Échec upload: " + t.getMessage());
                }
            });
    }

    public void uploadText(String text) {
        apiInterface.uploadText(text)
            .enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call,
                                    retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Texte envoyé avec succès");
                    } else {
                        Log.e(TAG, "Erreur envoi texte: " + response.code());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Échec envoi texte: " + t.getMessage());
                }
            });
    }

    private String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            default:
                return "application/octet-stream";
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}