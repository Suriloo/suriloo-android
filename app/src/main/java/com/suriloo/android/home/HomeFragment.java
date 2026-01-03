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

import com.suriloo.android.ApiClient;
import com.suriloo.android.ApiService;
import com.suriloo.android.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CategoryAdapter.OnCategorySelectedListener {

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

    private List<Content> popularChoiceList;
    private List<Content> recommendationList;
    private List<Content> recentlyWatchedList;
    private List<Content> newArrivalsList;
    private List<String> categoryList;

    private ApiService apiService;
    private Call<List<Content>> popularChoiceCall;
    private Call<List<Content>> recommendationCall;
    private Call<List<Content>> recentlyWatchedCall;
    private Call<List<Content>> newArrivalsCall;

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
        fetchCategories();
        popularChoiceList = new ArrayList<>();
        recommendationList = new ArrayList<>();
        recentlyWatchedList = new ArrayList<>();
        newArrivalsList = new ArrayList<>();

        // 3. Setup Adapters
        popularChoiceAdapter = new ContentAdapter(popularChoiceList);
        if (popularChoice != null) {
            popularChoice.setAdapter(popularChoiceAdapter);
        } else {
            Log.e("HomeFragment", "popularChoice ViewPager2 not found");
        }

        recommendationCardAdapter = new CardAdapter(recommendationList);
        if (recommendationCard != null) {
            recommendationCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recommendationCard.setAdapter(recommendationCardAdapter);
        } else {
            Log.e("HomeFragment", "recommendationCard RecyclerView not found");
        }

        recentlyWatchedCardAdapter = new CardAdapter(recentlyWatchedList);
        if (recentlyWatchedCard != null) {
            recentlyWatchedCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recentlyWatchedCard.setAdapter(recentlyWatchedCardAdapter);
        } else {
            Log.e("HomeFragment", "recentlyWatchedCard RecyclerView not found");
        }

        newArrivalsCardAdapter = new CardAdapter(newArrivalsList);
        if (newArrivalsCard != null) {
            newArrivalsCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            newArrivalsCard.setAdapter(newArrivalsCardAdapter);
        } else {
            Log.e("HomeFragment", "newArrivalsCard RecyclerView not found");
        }

        categoryAdapter = new CategoryAdapter(categoryList, this);
        if (categoryRecyclerView != null) {
            categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            categoryRecyclerView.setAdapter(categoryAdapter);
        } else {
            Log.e("HomeFragment", "categoryRecyclerView not found");
        }

        setupBannerTransformers();
        loadInitialContent();

        return view;
    }

    @Override
    public void onCategorySelected(String category) {
        // Cancel any previous calls to avoid race conditions
        if (recommendationCall != null) recommendationCall.cancel();
        if (recentlyWatchedCall != null) recentlyWatchedCall.cancel();
        if (newArrivalsCall != null) newArrivalsCall.cancel();

        // Make new API calls
        updateRecommendation(category);
        updateRecentlyWatched(category);
        updateNewArrivals(category);
    }

    private void loadInitialContent() {
        updatePopularChoice();
        updateRecommendation(null);
        updateRecentlyWatched(null);
        updateNewArrivals(null);
    }

    private void updatePopularChoice() {
        popularChoiceCall = apiService.getPopularContent();
        popularChoiceCall.enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Content> content = response.body();
                    popularChoiceList.clear();
                    if (content.size() > 1) {
                        popularChoiceList.add(content.get(content.size() - 1)); // Add last item to the beginning
                        popularChoiceList.addAll(content);
                        popularChoiceList.add(content.get(0)); // Add first item to the end
                    } else {
                        popularChoiceList.addAll(content);
                    }
                    popularChoiceAdapter.notifyDataSetChanged();
                    if (popularChoiceList.size() > 1 && popularChoice != null) {
                        popularChoice.setCurrentItem(1, false);
                    }
                } else {
                    Log.e("HomeFragment", "Popular choice API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e("HomeFragment", "Popular choice API call failed: " + t.getMessage());
                }
            }
        });
    }

    private void updateRecommendation(String category) {
        recommendationCall = (category == null) ? apiService.getRecommendedContent() : apiService.getRecommendationByCategory(category);
        recommendationCall.enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recommendationList.clear();
                    recommendationList.addAll(response.body());
                    recommendationCardAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Recommendation API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e("HomeFragment", "Recommendation API call failed: " + t.getMessage());
                }
            }
        });
    }

    private void updateRecentlyWatched(String category) {
        recentlyWatchedCall = (category == null) ? apiService.getRecentlyWatchedContent() : apiService.getRecentlyWatchedByCategory(category);
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

    private void updateNewArrivals(String category) {
        newArrivalsCall = (category == null) ? apiService.getNewArrivalsContent() : apiService.getNewArrivalsByCategory(category);
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

    private void fetchCategories() {
        categoryList = new ArrayList<>();
        categoryList.add("Action");
        categoryList.add("Comedy");
        categoryList.add("Drama");
        categoryList.add("Horror");
        categoryList.add("Science Fiction");
        categoryList.add("Fantasy");
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
}
