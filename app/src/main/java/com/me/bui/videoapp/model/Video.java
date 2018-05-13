package com.me.bui.videoapp.model;

import java.util.ArrayList;

/**
 * Created by mao.bui on 5/12/2018.
 */
public class Video {

    private String mVideoId;
    private String mTitle;
    private String mDescription;
    private String mThumnailUrl;

    public Video(String videoId, String title, String description, String thumnailUrl) {
        mVideoId = videoId;
        mTitle = title;
        mDescription = description;
        mThumnailUrl = thumnailUrl;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getThumnailUrl() {
        return mThumnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        mThumnailUrl = thumnailUrl;
    }
}
