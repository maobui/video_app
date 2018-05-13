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
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.me.bui.videoapp.adapter.VideoAdapter;
import com.me.bui.videoapp.model.Video;
import com.me.bui.videoapp.viewmodel.VideoViewModel;
import com.me.bui.videoapp.viewmodel.VideoViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import static com.me.bui.videoapp.Config.TYPE_VIDEO_RELATE;
import static com.me.bui.videoapp.MainActivity.EXTRA_VIDEO_DESC;
import static com.me.bui.videoapp.MainActivity.EXTRA_VIDEO_ID;
import static com.me.bui.videoapp.MainActivity.EXTRA_VIDEO_TITLE;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.ItemClickListener, YouTubePlayer.OnInitializedListener {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final int RECOVERY_REQUEST = 1;
    private Video mCurrentVideo;

    private TextView mTvTitle;
    private TextView mTvDesc;
    private ProgressBar mProgressBar;
    private ArrayList<Video> mVideoArrayList;
    private RecyclerView mRecyclerViewDetail;
    private VideoAdapter mAdapter;
    private YouTubePlayerSupportFragment mTubePlayerFragment;
    private YouTubePlayer mTubePlayer;

    private VideoViewModel mVideoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupViews();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_VIDEO_ID)) {
            String videoId = intent.getStringExtra(EXTRA_VIDEO_ID);
            String title = intent.getStringExtra(EXTRA_VIDEO_TITLE);
            String desc = intent.getStringExtra(EXTRA_VIDEO_DESC);
            mCurrentVideo = new Video(videoId, title, desc, "");
            updateUI();
        }

        if (mCurrentVideo != null) {
            setupViewModel();
        }

    }

    private void setupViews() {
        getSupportActionBar().hide();
        mVideoArrayList = new ArrayList<>();
        mTvTitle = findViewById(R.id.tv_title);
        mTvDesc = findViewById(R.id.tv_desc);
        mProgressBar = findViewById(R.id.prog_loading);
        mRecyclerViewDetail = findViewById(R.id.recycler_view_detail);
        mTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        mTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        mRecyclerViewDetail.setLayoutManager(layoutManager);
        mRecyclerViewDetail.setHasFixedSize(true);
        mAdapter = new VideoAdapter(this, mVideoArrayList, this );
        mRecyclerViewDetail.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        VideoViewModelFactory factory = new VideoViewModelFactory(AppController.getInstance(), TYPE_VIDEO_RELATE, mCurrentVideo.getVideoId());
        mVideoViewModel = ViewModelProviders.of(this, factory).get(VideoViewModel.class);
        mVideoViewModel.getVideoList().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> videos) {
                Log.e(TAG, "Updating list video of task from LiveData in ViewModel.");
                mAdapter.setListVideo(videos);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateUI () {
        mTvTitle.setText(mCurrentVideo.getTitle());
        mTvDesc.setText(mCurrentVideo.getDescription());
        playVideo(mCurrentVideo.getVideoId());
    }

    @Override
    public void onItemClickListener(int itemId) {
        refreshData(itemId);
        updateUI();
    }

    private void refreshData(int itemId) {
        mCurrentVideo = mAdapter.getItem(itemId);
        mVideoArrayList.clear();
        mVideoViewModel.refreshVideoList(TYPE_VIDEO_RELATE, mCurrentVideo.getVideoId()).observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> videos) {
                Log.e(TAG, "Refreshing list video of task from LiveData in ViewModel.");
                mAdapter.setListVideo(videos);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mTubePlayer = youTubePlayer;
        playVideo(mCurrentVideo.getVideoId());
    }

    private void playVideo(String videoId) {
        if (mTubePlayer != null) {
            mTubePlayer.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST);
        } else {
            Toast.makeText(this, R.string.error_init_youtube_player, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider () {
        return mTubePlayerFragment;
    }
}
