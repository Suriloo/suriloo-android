package com.suriloo.android.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suriloo.android.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 popularChoice;
    private RecyclerView recommendationCard;
    private RecyclerView recentlyWatchedCard;
    private RecyclerView newArrivalsCard;

    private ContentAdapter popularChoiceAdapter;
    private CardAdapter recommendationCardAdapter;
    private CardAdapter recentlyWatchedCardAdapter;

    private CardAdapter newArrivalsCardAdapter;
    private List<Content> popularChoiceList;
    private List<Content> recommendationList;
    private List<Content> recentlyWatchedList;
    private List<Content> newArrivalsList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Initialize Views
        popularChoice = view.findViewById(R.id.popularChoice);
        recommendationCard = view.findViewById(R.id.recommendationCard);
        recentlyWatchedCard = view.findViewById(R.id.recentlyWatchedCard);
        newArrivalsCard = view.findViewById(R.id.newArrivalsCard);

        // 2. Prepare Dummy Data (Replace with API fetch later)
        fetchPopularChoice();
        fetchRecommendation();
        fetchRecentlyWatched();
        fetchnewArrivals();

        // 3. Setup Adapters
        popularChoiceAdapter = new ContentAdapter(popularChoiceList);
        popularChoice.setAdapter(popularChoiceAdapter);

        if (popularChoiceList.size() > 1) {
            popularChoice.setCurrentItem(1, false);
        }

        recommendationCardAdapter = new CardAdapter(recommendationList);
        recommendationCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendationCard.setAdapter(recommendationCardAdapter);

        recentlyWatchedCardAdapter = new CardAdapter(recentlyWatchedList);
        recentlyWatchedCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentlyWatchedCard.setAdapter(recentlyWatchedCardAdapter);


        newArrivalsCardAdapter = new CardAdapter(newArrivalsList);
        newArrivalsCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newArrivalsCard.setAdapter(newArrivalsCardAdapter);
        
        setupBannerTransformers();

        return view;
    }

    private void fetchPopularChoice() {
        List<Content> originalList = new ArrayList<>();
        // Note: Using placeholder images for now
        originalList.add(new Content("Popular Choice 1", "https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+1"));
        originalList.add(new Content("Popular choice 2", "https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+2"));
        originalList.add(new Content("Popular choice 3", "https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+3"));

        popularChoiceList = new ArrayList<>();
        if (originalList.size() > 1) {
            popularChoiceList.add(originalList.get(originalList.size() - 1)); // Add last item to the beginning
            popularChoiceList.addAll(originalList);
            popularChoiceList.add(originalList.get(0)); // Add first item to the end
        } else {
            popularChoiceList.addAll(originalList);
        }
    }



    private void fetchRecommendation(){
         recommendationList = new ArrayList<>();
        // Note: Using placeholder images for now
        recommendationList.add(new Content("recommendation 1", "https://scontent.fktm10-1.fna.fbcdn.net/v/t39.30808-6/605762764_1268296745332219_7347772906103546981_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=833d8c&_nc_ohc=BJZXiDuabJ8Q7kNvwHlwOX7&_nc_oc=AdlopyuBCtk3aVvZr6jLWDvvXVGyE6RHG_8Fn5-E5uBADXECV_Ra4-sLg3PRnPcJvG8-xLW1aJTCdAX6SjELGfMJ&_nc_zt=23&_nc_ht=scontent.fktm10-1.fna&_nc_gid=pnwJGYzr3CF6X6kcAj8VoA&oh=00_AfkeGfFgNNMmxJqhP_hiby4IngGprydPEGszdCZtcT430Q&oe=69558862"));
        recommendationList.add(new Content("recommendation 2  ","https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+2"));
        recommendationList.add(new Content("recommendation 3", "https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+3"));
    }
    private void fetchRecentlyWatched(){
        recentlyWatchedList = new ArrayList<>();
        // Note: Using placeholder images for now
        recentlyWatchedList.add(new Content("Watched 1", "https://scontent.fktm10-1.fna.fbcdn.net/v/t39.30808-6/605762764_1268296745332219_7347772906103546981_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=833d8c&_nc_ohc=BJZXiDuabJ8Q7kNvwHlwOX7&_nc_oc=AdlopyuBCtk3aVvZr6jLWDvvXVGyE6RHG_8Fn5-E5uBADXECV_Ra4-sLg3PRnPcJvG8-xLW1aJTCdAX6SjELGfMJ&_nc_zt=23&_nc_ht=scontent.fktm10-1.fna&_nc_gid=pnwJGYzr3CF6X6kcAj8VoA&oh=00_AfkeGfFgNNMmxJqhP_hiby4IngGprydPEGszdCZtcT430Q&oe=69558862"));
        recentlyWatchedList.add(new Content("Watched 2  ","https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+2"));
        recentlyWatchedList.add(new Content("Watched 3", "https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+3"));
    }
    private void fetchnewArrivals(){
        newArrivalsList = new ArrayList<>();
        // Note: Using placeholder images for now
        newArrivalsList.add(new Content("New Arrivals 1", "https://scontent.fktm10-1.fna.fbcdn.net/v/t39.30808-6/605762764_1268296745332219_7347772906103546981_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=833d8c&_nc_ohc=BJZXiDuabJ8Q7kNvwHlwOX7&_nc_oc=AdlopyuBCtk3aVvZr6jLWDvvXVGyE6RHG_8Fn5-E5uBADXECV_Ra4-sLg3PRnPcJvG8-xLW1aJTCdAX6SjELGfMJ&_nc_zt=23&_nc_ht=scontent.fktm10-1.fna&_nc_gid=pnwJGYzr3CF6X6kcAj8VoA&oh=00_AfkeGfFgNNMmxJqhP_hiby4IngGprydPEGszdCZtcT430Q&oe=69558862"));
        newArrivalsList.add(new Content("New Arrivals 2  ","https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+2"));
        newArrivalsList.add(new Content("New Arrivals 3", "https://via.placeholder.com/400x600/000000/FFFFFF?text=Movie+3"));
    }


    private void setupBannerTransformers() {
        popularChoice.setClipToPadding(false);
        popularChoice.setClipChildren(false);
        popularChoice.setOffscreenPageLimit(3);

        // This creates the 'peek' at the next and previous banners
        popularChoice.setPadding(160, 0, 160, 0);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(20));

        // The Zoom-out/fade effect
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.8f + r * 0.2f);
        });

        popularChoice.setPageTransformer(transformer);

        if (popularChoiceList.size() > 1) {
            popularChoice.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        int currentItem = popularChoice.getCurrentItem();
                        if (currentItem == 0) {
                            popularChoice.setCurrentItem(popularChoiceList.size() - 2, false);
                        } else if (currentItem == popularChoiceList.size() - 1) {
                            popularChoice.setCurrentItem(1, false);
                        }
                    }
                }
            });
        }
    }
}
