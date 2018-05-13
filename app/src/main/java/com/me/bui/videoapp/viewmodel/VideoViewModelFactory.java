package com.me.bui.videoapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by mao.bui on 5/13/2018.
 */
public class VideoViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context mContext;
    private int mType;
    private String mStrQuery;

    public VideoViewModelFactory(Context context, int type, String strQuery) {
        mContext = context;
        mType = type;
        mStrQuery = strQuery;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VideoViewModel(mContext, mType, mStrQuery);
    }
}
