package com.suriloo.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suriloo.android.home.Content;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryContentFragment extends Fragment {

    private RecyclerView categoryContentRecyclerView;
    private ContentListAdapter contentListAdapter;
    private List<Content> contentList;
    private String category;
    private ApiService apiService;

    public CategoryContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_content, container, false);

        TextView categoryTitleTextView = view.findViewById(R.id.category_title_text_view);
        categoryTitleTextView.setText(category);

        apiService = ApiClient.getInstance(getContext()).create(ApiService.class);

        categoryContentRecyclerView = view.findViewById(R.id.category_content_recycler_view);

        contentList = new ArrayList<>();
        contentListAdapter = new ContentListAdapter(contentList);
        categoryContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryContentRecyclerView.setAdapter(contentListAdapter);

        fetchContentData();

        return view;
    }

    private void fetchContentData() {
        apiService.getContentByCategory(category.toLowerCase()).enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contentList.clear();
                    contentList.addAll(response.body());
                    contentListAdapter.notifyDataSetChanged();
                } else {
                    Log.e("CategoryContentFragment", "API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                Log.e("CategoryContentFragment", "API call failed: " + t.getMessage());
            }
        });
    }
}
