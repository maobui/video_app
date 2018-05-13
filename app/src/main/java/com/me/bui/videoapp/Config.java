package com.me.bui.videoapp;

/**
 * Created by mao.bui on 5/13/2018.
 */
public final class Config {

    private Config () {}

    public static final String YOUTUBE_API_KEY = "AIzaSyClCmx5cTujSTrlwsDA63QZWi7tdSXqxSI";
    public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/search?";
    public static final String BASE_URL_QUERY = BASE_URL + "key=" + YOUTUBE_API_KEY;

    public static final int TYPE_VIDEO_CHANNEL = 0;
    public static final int TYPE_VIDEO_RELATE = 1;
}
