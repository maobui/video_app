package com.me.bui.videoapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.me.bui.videoapp.AppController;
import com.me.bui.videoapp.R;
import com.me.bui.videoapp.model.Video;

import java.util.List;

/**
 * Created by mao.bui on 5/12/2018.
 */
public class VideoAdapter extends RecyclerView.Adapter <VideoAdapter.VideoViewHolder>{

    private Context mContext;
    private List<Video> mVideos;
    private ItemClickListener mClickListener;
    private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

    public VideoAdapter(Context context, List<Video> videos, ItemClickListener itemClickListener) {
        mContext = context;
        mVideos = videos;
        mClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return mVideos == null ? 0 : mVideos.size();
    }

    public Video getItem (int position) {
        return mVideos == null ? null : mVideos.get(position);
    }

    public void setListVideo(List<Video> videos) {
//        mVideos = videos;
        mVideos.addAll(videos);
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private NetworkImageView mImgThumnail;
        private TextView mTvTitle;
        private TextView mTvDescription;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mImgThumnail = itemView.findViewById(R.id.img_thumnail);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvDescription = itemView.findViewById(R.id.tv_desc);
            itemView.setOnClickListener(this);
        }

        public void bind (Video video) {
            if (video == null)
                return;
            if(mImageLoader == null)
                mImageLoader = AppController.getInstance().getImageLoader();
            mImgThumnail.setImageUrl(video.getThumnailUrl(), mImageLoader);
            mTvTitle.setText(video.getTitle());
            mTvDescription.setText(video.getDescription());
        }

        @Override
        public void onClick(View view) {
            int elementId = getAdapterPosition();
            mClickListener.onItemClickListener(elementId);
        }
    }
}
