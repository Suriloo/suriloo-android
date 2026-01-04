package com.suriloo.android.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.suriloo.android.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    // 1. Listener Interface
    public interface OnCategoryClickListener {
        void onCategoryClick(Content content);
    }

    private final List<Content> contentList;
    private final OnCategoryClickListener clickListener;

    // 2. Updated Constructor to accept the listener
    public CategoryAdapter(List<Content> contentList, OnCategoryClickListener clickListener) {
        this.contentList = contentList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_chip, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Content contentItem = contentList.get(position);
        holder.chip.setText(contentItem.getTitle());

        // 3. The adapter now just reports the click, it doesn't navigate
        holder.chip.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCategoryClick(contentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        final Chip chip;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip_item);
        }
    }
}
