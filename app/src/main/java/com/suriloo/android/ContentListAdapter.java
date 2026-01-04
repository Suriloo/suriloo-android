package com.suriloo.android;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.suriloo.android.home.Content;

import java.io.Serializable;
import java.util.List;

public class ContentListAdapter extends RecyclerView.Adapter<ContentListAdapter.ViewHolder> {

    private final List<Content> contentList;

    public ContentListAdapter(List<Content> contentList) {
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Content content = contentList.get(position);
        holder.name.setText(content.getTitle());
        holder.author.setText(content.getAuthor());

        if (content.getImageUrl() != null) {
            String fullImageUrl = ApiClient.BASE_URL + content.getImageUrl();
            Glide.with(holder.itemView.getContext())
                    .load(fullImageUrl)
                    .placeholder(R.drawable.main_logo)
                    .into(holder.cover);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlayerActivity.class);
            // Pass the entire list and the clicked position
            intent.putExtra("playlist", (Serializable) contentList);
            intent.putExtra("position", position);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;
        TextView author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.img_item_cover);
            name = itemView.findViewById(R.id.txt_item_name);
            author = itemView.findViewById(R.id.txt_item_author);
        }
    }
}
