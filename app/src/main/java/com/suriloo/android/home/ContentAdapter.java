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
import com.suriloo.android.model.PopularChoiceResponse; // Import your new model

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.BannerViewHolder> {

    private List<PopularChoiceResponse> bannerList;

    // We need the server URL to load images correctly
    // e.g., "http://192.168.1.5:8080"
    private String baseUrl;

    // Constructor accepts the list AND the base URL
    public ContentAdapter(List<PopularChoiceResponse> bannerList, String baseUrl) {
        this.bannerList = bannerList;
        this.baseUrl = baseUrl;
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
            return;
        }

        // Infinite scroll logic
        PopularChoiceResponse item = bannerList.get(position % bannerList.size());

        holder.bannerTitle.setText(item.getTitle());

        // CONSTRUCT FULL IMAGE URL
        // Server gives: "/api/music/art/song.mp3"
        // We need: "http://192.168.1.5:8080/api/music/art/song.mp3"
        String fullImageUrl = baseUrl + item.getImageUrl();

        Glide.with(holder.itemView.getContext())
                .load(fullImageUrl)
                .placeholder(R.drawable.main_logo)
                .centerCrop()
                .into(holder.bannerImage);

        // OPTIONAL: Handle click to play music later
        holder.itemView.setOnClickListener(v -> {
            // You can access item.getContentUrl() here later!
        });
    }

    @Override
    public int getItemCount() {
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