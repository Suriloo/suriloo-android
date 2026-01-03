package com.suriloo.android.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.suriloo.android.CategoryContentFragment;
import com.suriloo.android.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_chip, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.chip.setText(category.getName());
        holder.chip.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            CategoryContentFragment fragment = new CategoryContentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", category.getName());
            fragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = (Chip) itemView.findViewById(R.id.chip_item);
        }
    }
}
