package com.example.myapplication1111;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoModel> videoList;

    public VideoAdapter(List<VideoModel> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoModel video = videoList.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.tvDescription.setText(video.getDescription());
        holder.tvChannel.setText(video.getChannelTitle());
        holder.tvPublishTime.setText("نُشر في: " + video.getPublishTime());

        // تحميل الصورة المصغرة باستعمال Glide
        Glide.with(holder.itemView.getContext())
                .load(video.getThumbnailUrl())
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvChannel, tvPublishTime;
        ImageView ivThumbnail;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvChannel = itemView.findViewById(R.id.tvChannel);
            tvPublishTime = itemView.findViewById(R.id.tvPublishTime);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}