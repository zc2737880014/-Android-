package com.fishstar.minitiktok.api;

import com.fishstar.minitiktok.SentActivity;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IMiniDouyinService {
    // TODO 7: Define IMiniDouyinService
    String HOST = "http://test.androidcamp.bytedance.com/mini_douyin/invoke/";

    String PATH ="video";

    @GET(PATH)
    Call<SentActivity.Feeds> getVideo();

    @Multipart
    @POST(PATH)
    Call<SentActivity.results> PostVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part video
    );

}
