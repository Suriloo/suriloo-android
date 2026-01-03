package com.suriloo.android.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.suriloo.android.R;


import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.BannerViewHolder> {

    private List<Content> bannerList;

    public ContentAdapter(List<Content> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popularchoice_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        if (bannerList == null || bannerList.isEmpty()) {
            return; // Don't do anything if the list is empty
        }

        Content content = bannerList.get(position % bannerList.size());

        holder.bannerTitle.setText(content.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(content.getImageUrl())
                .placeholder(R.drawable.main_logo)
                .centerCrop() // Ensures the image fills the banner area
                .into(holder.bannerImage);
    }

    @Override
    public int getItemCount() {
        // Return 0 if the list is empty, otherwise return a large number for infinite looping
        return (bannerList == null || bannerList.isEmpty()) ? 0 : Integer.MAX_VALUE;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        TextView bannerTitle;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.bannerImage);
            bannerTitle = itemView.findViewById(R.id.bannerTitle);
        }
    }
}