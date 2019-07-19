package com.fishstar.minitiktok.DataType;

public class VideoInfo {
    private String studentId;
    private  String userName;
    private String imageUrl;
    private String videoUrl;

    public VideoInfo(){
        studentId="3170101504";
        userName="游鱼";
        imageUrl = "https://lf3-hscdn-tos.pstatp.com/obj/developer-baas/baas/tt7217xbo2wz3cem41/f7043544e3f7e05d_1563114220809.jpg";
        videoUrl ="https://lf6-hscdn-tos.pstatp.com/obj/developer-baas/baas/tt7217xbo2wz3cem41/d6c3ebbfb886634b_1563114220987.mp4";
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
