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
                .inflate(com.suriloo.android.R.layout.popularchoice_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Content content = bannerList.get(position % bannerList.size());

        holder.bannerTitle.setText(content.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(content.getImageUrl())
                .placeholder(com.suriloo.android.R.drawable.main_logo)
                .centerCrop() // Ensures the image fills the banner area
                .into(holder.bannerImage);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        TextView bannerTitle;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(com.suriloo.android.R.id.bannerImage);
            bannerTitle = itemView.findViewById(com.suriloo.android.R.id.bannerTitle);
        }
    }
}