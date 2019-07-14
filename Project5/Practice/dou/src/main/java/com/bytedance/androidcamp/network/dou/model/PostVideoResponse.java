package com.bytedance.androidcamp.network.dou.model;

import com.google.gson.annotations.SerializedName;

public class PostVideoResponse {
    @SerializedName("url") private String url;
    @SerializedName("success") private boolean success;
    public boolean isSuccess(){
        return success;
    }
    public String getUrl(){
        return url;
    }
}
