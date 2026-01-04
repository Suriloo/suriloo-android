package com.suriloo.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suriloo.android.home.CardAdapter;
import com.suriloo.android.home.Content;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private RecyclerView exploreRecyclerView;
    private CardAdapter cardAdapter;
    private List<Content> exploreList;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        exploreRecyclerView = view.findViewById(R.id.explore_recycler_view);

        fetchExploreData();

        cardAdapter = new CardAdapter(exploreList);
        exploreRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        exploreRecyclerView.setAdapter(cardAdapter);

        return view;
    }

    private void fetchExploreData() {
        exploreList = new ArrayList<>();
        exploreList.add(new Content("FM", "https://via.placeholder.com/200x300/FF0000/FFFFFF?text=Movies"));
        exploreList.add(new Content("Religious", "https://via.placeholder.com/200x300/00FF00/FFFFFF?text=Series"));
        exploreList.add(new Content("", "https://via.placeholder.com/200x300/0000FF/FFFFFF?text=Documentaries"));
        exploreList.add(new Content("Anime", "https://via.placeholder.com/200x300/FFFF00/000000?text=Anime"));
        exploreList.add(new Content("Live TV", "https://via.placeholder.com/200x300/FF00FF/FFFFFF?text=Live+TV"));
        exploreList.add(new Content("Sports", "https://via.placeholder.com/200x300/00FFFF/000000?text=Sports"));
        exploreList.add(new Content("Kids", "https://via.placeholder.com/200x300/F0F0F0/000000?text=Kids"));
        exploreList.add(new Content("Music", "https://via.placeholder.com/200x300/0F0F0F/FFFFFF?text=Music"));
    }
}
