package com.me.bui.videoapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.bui.videoapp.adapter.VideoAdapter;
import com.me.bui.videoapp.model.Video;
import com.me.bui.videoapp.util.Utils;
import com.me.bui.videoapp.viewmodel.VideoViewModel;
import com.me.bui.videoapp.viewmodel.VideoViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VideoAdapter.ItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String CHANNEL_QUERY = "UCUTtZDz_PKCBNcMEU8gSIqg";

    public static final String EXTRA_VIDEO_ID = "extraVideoId";
    public static final String EXTRA_VIDEO_TITLE = "extraVideoTitle";
    public static final String EXTRA_VIDEO_DESC = "extraVideoDesc";

    private ProgressBar mProgressBar;
    private List<Video> mVideoArrayList;
    private RecyclerView mRecyclerViewMain;
    private VideoAdapter mAdapter;
    private TextView mTvEmptyView;

    private VideoViewModel mVideoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();

        if (Utils.hasConnect()) {
            setupViewModel();
        } else {
            mTvEmptyView.setText(R.string.no_internet_connection);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setupViews() {
        mVideoArrayList = new ArrayList<Video>();
        mProgressBar = findViewById(R.id.prog_loading);
        mRecyclerViewMain = findViewById(R.id.recycler_view_main);
        mTvEmptyView = findViewById(R.id.tv_empty_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewMain.setLayoutManager(layoutManager);
        mRecyclerViewMain.setHasFixedSize(true);
        mAdapter = new VideoAdapter(this, mVideoArrayList, this);
        mRecyclerViewMain.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        VideoViewModelFactory factory = new VideoViewModelFactory(AppController.getInstance(), Config.TYPE_VIDEO_CHANNEL, CHANNEL_QUERY);
        mVideoViewModel = ViewModelProviders.of(this, factory).get(VideoViewModel.class);
        mVideoViewModel.getVideoList().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> videos) {
                Log.e(TAG, "Updating list video of task from LiveData in ViewModel.");
                updateUi(videos);
            }
        });
    }

    private void updateUi(@Nullable List<Video> videos) {
        mTvEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setListVideo(videos);
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Video video = mAdapter.getItem(itemId);
        intent.putExtra(EXTRA_VIDEO_ID, video.getVideoId());
        intent.putExtra(EXTRA_VIDEO_TITLE, video.getTitle());
        intent.putExtra(EXTRA_VIDEO_DESC, video.getDescription());
        startActivity(intent);
    }
}
