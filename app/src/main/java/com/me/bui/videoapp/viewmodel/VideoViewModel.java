package com.me.bui.videoapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.me.bui.videoapp.model.Video;

import java.util.List;

/**
 * Created by mao.bui on 5/13/2018.
 */
public class VideoViewModel extends ViewModel{
    private static final String TAG = VideoViewModel.class.getSimpleName();

    private Context mContext;
    private ListVideoLiveData mData;

    public VideoViewModel(Context context, int type, String strQuery) {
        mContext = context;
        if (mData == null) {
            mData = new ListVideoLiveData(context, type, strQuery);
        }
    }

    public MutableLiveData<List<Video>> getVideoList() {
        return mData;
    }

    public MutableLiveData<List<Video>> refreshVideoList(int type, String query) {
        Log.e(TAG, "------------------- refreshVideoList mStrQuery : " + query);
        return new ListVideoLiveData(mContext, type, query);
    }
}
