package com.suriloo.android.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.suriloo.android.ApiClient;
import com.suriloo.android.ApiService;
import com.suriloo.android.CategoryContentFragment;
import com.suriloo.android.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 1. Implement the listener interface
public class HomeFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private ViewPager2 popularChoice;
    private RecyclerView recommendationCard;
    private RecyclerView recentlyWatchedCard;
    private RecyclerView newArrivalsCard;
    private RecyclerView categoryRecyclerView;
    private LinearLayout recentlyWatchedTitleLayout;

    private ContentAdapter popularChoiceAdapter;
    private CardAdapter recommendationCardAdapter;
    private CardAdapter recentlyWatchedCardAdapter;
    private CardAdapter newArrivalsCardAdapter;
    private CategoryAdapter categoryAdapter;

    private List<Content> popularChoiceList;
    private List<Content> recommendationList;
    private List<Content> recentlyWatchedList;
    private List<Content> newArrivalsList;
    private List<Content> categoryContentList;

    private ApiService apiService;
    private Call<List<Content>> popularChoiceCall;
    private Call<List<Content>> recommendationCall;
    private Call<List<Content>> recentlyWatchedCall;
    private Call<List<Content>> newArrivalsCall;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        popularChoice = view.findViewById(R.id.popularChoice);
        recommendationCard = view.findViewById(R.id.recommendationCard);
        recentlyWatchedCard = view.findViewById(R.id.recentlyWatchedCard);
        newArrivalsCard = view.findViewById(R.id.newArrivalsCard);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        recentlyWatchedTitleLayout = view.findViewById(R.id.recently_watched_title_layout);

        apiService = ApiClient.getInstance(getContext()).create(ApiService.class);

        popularChoiceList = new ArrayList<>();
        recommendationList = new ArrayList<>();
        recentlyWatchedList = new ArrayList<>();
        newArrivalsList = new ArrayList<>();
        categoryContentList = new ArrayList<>();

        popularChoiceAdapter = new ContentAdapter(popularChoiceList);
        popularChoice.setAdapter(popularChoiceAdapter);

        recommendationCardAdapter = new CardAdapter(recommendationList);
        recommendationCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendationCard.setAdapter(recommendationCardAdapter);

        recentlyWatchedCardAdapter = new CardAdapter(recentlyWatchedList);
        recentlyWatchedCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recentlyWatchedCard.setAdapter(recentlyWatchedCardAdapter);

        newArrivalsCardAdapter = new CardAdapter(newArrivalsList);
        newArrivalsCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newArrivalsCard.setAdapter(newArrivalsCardAdapter);

        // 2. Pass the fragment itself (as the listener) to the adapter
        categoryAdapter = new CategoryAdapter(categoryContentList, this);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        setupBannerTransformers();
        loadInitialContent();

        return view;
    }

    // 3. Handle the click event here, in the fragment
    @Override
    public void onCategoryClick(Content content) {
        CategoryContentFragment fragment = new CategoryContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", content.getTitle());
        fragment.setArguments(bundle);

        // Use requireActivity() which is safer in fragments
        ((AppCompatActivity) requireActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadInitialContent() {
        updatePopularChoice();
        updateRecommendation();
        updateRecentlyWatched();
        updateNewArrivals();
        updateCategories();
    }

    private void updateCategories() {
        categoryContentList.clear();
        categoryContentList.add(new Content("FM", ""));
        categoryContentList.add(new Content("Movies", ""));
        categoryContentList.add(new Content("Series", ""));
        categoryContentList.add(new Content("Documentaries", ""));
        categoryContentList.add(new Content("Anime", ""));
        categoryContentList.add(new Content("Live TV", ""));
        categoryContentList.add(new Content("Sports", ""));
        categoryContentList.add(new Content("Kids", ""));
        categoryContentList.add(new Content("Music", ""));
        categoryAdapter.notifyDataSetChanged();
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
                        popularChoiceList.add(content.get(content.size() - 1));
                        popularChoiceList.addAll(content);
                        popularChoiceList.add(content.get(0));
                    } else {
                        popularChoiceList.addAll(content);
                    }
                    popularChoiceAdapter.notifyDataSetChanged();
                    if (popularChoiceList.size() > 1) {
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

    private void updateRecommendation() {
        recommendationCall = apiService.getRecommendedContent();
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

    private void updateRecentlyWatched() {
        recentlyWatchedCall = apiService.getMostRecentlyWatched();
        recentlyWatchedCall.enqueue(new Callback<List<Content>>() {
            @Override
            public void onResponse(Call<List<Content>> call, Response<List<Content>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    recentlyWatchedTitleLayout.setVisibility(View.VISIBLE);
                    recentlyWatchedCard.setVisibility(View.VISIBLE);
                    recentlyWatchedList.clear();
                    recentlyWatchedList.addAll(response.body());
                    recentlyWatchedCardAdapter.notifyDataSetChanged();
                } else {
                    recentlyWatchedTitleLayout.setVisibility(View.GONE);
                    recentlyWatchedCard.setVisibility(View.GONE);
                    Log.e("HomeFragment", "Recently watched API call failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Content>> call, Throwable t) {
                if (!call.isCanceled()) {
                    recentlyWatchedTitleLayout.setVisibility(View.GONE);
                    recentlyWatchedCard.setVisibility(View.GONE);
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
        popularChoice.setPadding(160, 0, 160, 0);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(20));
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
