package com.suriloo.android.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.suriloo.android.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategorySelectedListener {
        void onCategorySelected(String category);
    }

    private final List<String> categories;
    private int selectedPosition = 0; // Default to first item selected
    private final OnCategorySelectedListener listener;

    public CategoryAdapter(List<String> categories, OnCategorySelectedListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_chip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.chip.setText(category);
        holder.chip.setChecked(position == selectedPosition);

        holder.chip.setOnClickListener(v -> {
            if (selectedPosition != holder.getAdapterPosition()) {
                int oldSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldSelectedPosition);
                notifyItemChanged(selectedPosition);
                if (listener != null) {
                    listener.onCategorySelected(categories.get(selectedPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = (Chip) itemView;
        }
    }
}
