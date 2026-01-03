package com.suriloo.android.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.suriloo.android.ApiClient;
import com.suriloo.android.ApiService;
import com.suriloo.android.R;
import com.suriloo.android.home.Content;
import com.suriloo.android.model.PopularChoiceResponse;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ViewPager2 popularChoice;
    private RecyclerView recommendationCard;
    private RecyclerView recentlyWatchedCard;
    private RecyclerView newArrivalsCard;
    private RecyclerView categoryRecyclerView;

    private ContentAdapter popularChoiceAdapter;
    private CardAdapter recommendationCardAdapter;
    private CardAdapter recentlyWatchedCardAdapter;
    private CardAdapter newArrivalsCardAdapter;
    private CategoryAdapter categoryAdapter;

    private List<PopularChoiceResponse> popularChoiceList;
    private List<PopularChoiceResponse> recommendationList;
    private List<Content> recentlyWatchedList;
    private List<Content> newArrivalsList;
    private List<Category> categoryList;

    private ApiService apiService;
    private Call<List<PopularChoiceResponse>> popularChoiceCall;
    private Call<List<Content>> recommendationCall;
    private Call<List<Content>> recentlyWatchedCall;
    private Call<List<Content>> newArrivalsCall;
    private Call<List<Category>> categoryCall;


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
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);


        // Initialize ApiService
        apiService = ApiClient.getInstance(getContext()).create(ApiService.class);

        // 2. Prepare Data
        popularChoiceList = new ArrayList<>();
        //recommendationList = new ArrayList<>();
        //recentlyWatchedList = new ArrayList<>();
        //newArrivalsList = new ArrayList<>();
        //categoryList = new ArrayList<>();

        // 3. Setup Adapters
        popularChoiceAdapter = new ContentAdapter(popularChoiceList, "http://192.168.1.7:8080");
        if (popularChoice != null) {
            popularChoice.setAdapter(popularChoiceAdapter);
        } else {
            Log.e("HomeFragment", "popularChoice ViewPager2 not found");
        }

//        recommendationCardAdapter = new CardAdapter(recommendationList);
//        if (recommendationCard != null) {
//            recommendationCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//            recommendationCard.setAdapter(recommendationCardAdapter);
//        } else {
//            Log.e("HomeFragment", "recommendationCard RecyclerView not found");
//        }
//
//        recentlyWatchedCardAdapter = new CardAdapter(recentlyWatchedList);
//        if (recentlyWatchedCard != null) {
//            recentlyWatchedCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//            recentlyWatchedCard.setAdapter(recentlyWatchedCardAdapter);
//        } else {
//            Log.e("HomeFragment", "recentlyWatchedCard RecyclerView not found");
//        }
//
//        newArrivalsCardAdapter = new CardAdapter(newArrivalsList);
//        if (newArrivalsCard != null) {
//            newArrivalsCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//            newArrivalsCard.setAdapter(newArrivalsCardAdapter);
//        } else {
//            Log.e("HomeFragment", "newArrivalsCard RecyclerView not found");
//        }
//
//        categoryAdapter = new CategoryAdapter(categoryList);
//        if (categoryRecyclerView != null) {
//            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//            categoryRecyclerView.setAdapter(categoryAdapter);
//        } else {
//            Log.e("HomeFragment", "categoryRecyclerView not found");
//        }

        setupBannerTransformers();
        loadInitialContent();

        return view;
    }

    private void loadInitialContent() {
        updatePopularChoice();
//        updateRecommendation();
//        updateRecentlyWatched();
//        updateNewArrivals();
//        updateCategories();
    }

    private void updateCategories() {
        categoryList.clear();
        categoryList.add(new Category("1", "Movies"));
        categoryList.add(new Category("2", "Series"));
        categoryList.add(new Category("3", "Documentaries"));
        categoryList.add(new Category("4", "Anime"));
        categoryList.add(new Category("5", "Live TV"));
        categoryList.add(new Category("6", "Sports"));
        categoryList.add(new Category("7", "Kids"));
        categoryList.add(new Category("8", "Music"));
        categoryAdapter.notifyDataSetChanged();
    }


    private void updatePopularChoice() {
        popularChoiceCall = apiService.getPopularContent();
        popularChoiceCall.enqueue(new Callback<List<PopularChoiceResponse>>() {
            @Override
            public void onResponse(Call<List<PopularChoiceResponse>> call, Response<List<PopularChoiceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularChoiceResponse> popularList = response.body();
                    ViewPager2 viewPager = requireView().findViewById(R.id.popularChoice);
                    String baseUrl = "http://192.168.1.7:8080";
                    ContentAdapter adapter = new ContentAdapter(popularList, baseUrl);
                    viewPager.setAdapter(adapter);
                    setupCarouselEffect(viewPager);
                }
            }

            @Override
            public void onFailure(Call<List<PopularChoiceResponse>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e("HomeFragment", "Popular choice API call failed: " + t.getMessage());
                }
            }
        });
    }

    private void updateRecommendation() {
            // 1. Change <Content> to <PopularChoiceResponse>
            Call<List<PopularChoiceResponse>> call = apiService.getPopularContent();

            call.enqueue(new Callback<List<PopularChoiceResponse>>() {
                @Override
                public void onResponse(Call<List<PopularChoiceResponse>> call, Response<List<PopularChoiceResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // 2. Clear old data
                        recommendationList.clear();

                        // 3. Add new data from server
                        recommendationList.addAll(response.body());

                        // 4. Notify adapter
                        recommendationCardAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("HomeFragment", "Recommendation API call failed with code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<PopularChoiceResponse>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Log.e("HomeFragment", "Recommendation API call failed: " + t.getMessage());
                    }
                }
            });
        }

    private void updateRecentlyWatched() {
        recentlyWatchedCall = apiService.getRecentlyWatchedContent();
        recentlyWatchedCall.enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recentlyWatchedList.clear();
                    recentlyWatchedList.addAll(response.body());
                    recentlyWatchedCardAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Recently watched API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e("HomeFragment", "Recently watched API call failed: " + t.getMessage());
                }
            }
        });
    }

    private void updateNewArrivals() {
        newArrivalsCall = apiService.getNewArrivalsContent();
        newArrivalsCall.enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newArrivalsList.clear();
                    newArrivalsList.addAll(response.body());
                    newArrivalsCardAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "New arrivals API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e("HomeFragment", "New arrivals API call failed: " + t.getMessage());
                }
            }
        });
    }

    private void setupBannerTransformers() {
        if (popularChoice == null) return;
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

    // Helper method to make the ViewPager look like a Gallery/Carousel
    private void setupCarouselEffect(ViewPager2 viewPager) {
        // 1. Preload side items so they are ready to show
        viewPager.setOffscreenPageLimit(3);

        // 2. Create the transformer
        CompositePageTransformer transformer = new CompositePageTransformer();

        // 3. Add Margin (Space between items)
        // 40 pixels is a good starting point
        transformer.addTransformer(new MarginPageTransformer(40));

        // 4. Add Scale Effect (Make side items smaller)
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f); // Scale from 85% to 100%
        });

        // 5. Apply it
        viewPager.setPageTransformer(transformer);
    }

}
