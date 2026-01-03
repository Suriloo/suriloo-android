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
        exploreList.add(new Content("Card 1", "https://via.placeholder.com/200x300/FF0000/FFFFFF?text=Card+1"));
        exploreList.add(new Content("Card 2", "https://via.placeholder.com/200x300/00FF00/FFFFFF?text=Card+2"));
        exploreList.add(new Content("Card 3", "https://via.placeholder.com/200x300/0000FF/FFFFFF?text=Card+3"));
        exploreList.add(new Content("Card 4", "https://via.placeholder.com/200x300/FFFF00/000000?text=Card+4"));
        exploreList.add(new Content("Card 5", "https://via.placeholder.com/200x300/FF00FF/FFFFFF?text=Card+5"));
        exploreList.add(new Content("Card 6", "https://via.placeholder.com/200x300/00FFFF/000000?text=Card+6"));
        exploreList.add(new Content("Card 7", "https://via.placeholder.com/200x300/F0F0F0/000000?text=Card+7"));
        exploreList.add(new Content("Card 8", "https://via.placeholder.com/200x300/0F0F0F/FFFFFF?text=Card+8"));
    }
}
