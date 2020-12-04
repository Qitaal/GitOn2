package com.example.giton2.view.detailuser;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.example.giton2.R;
import com.example.giton2.adapter.UserListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailUserActivity extends AppCompatActivity {
    private static final String TAG = "DetailUserActivity";
    TextView tvName, tvRepository, tvFollowers, tvFollowing, tvLocation, tvCompany, tvBio;
    CircleImageView civUser;
    FloatingActionButton fabFavoriteUser;
    String USER_NAME;
    static DetailUserViewModel detailUserViewModel;

    @StringRes
    final int[] NAME_PAGES = new int[]{
            R.string.user_Followers,
            R.string.user_following
    };
    ViewPager2 vpFollowingFollowers;
    ViewPagerAdapter vpAdapter;
    TabLayout tlFollowersFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        detailUserViewModel = new ViewModelProvider(this).get(DetailUserViewModel.class);
        if (DetailUserActivityArgs.fromBundle(getIntent().getExtras()).getName() != null) {
            USER_NAME = DetailUserActivityArgs.fromBundle(getIntent().getExtras()).getName();
        } else {
            USER_NAME = getIntent().getStringExtra("USER");
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(USER_NAME);

        civUser = findViewById(R.id.civ_detail_user);
        tvName = findViewById(R.id.tv_name_detail_user);
        tvRepository = findViewById(R.id.tv_repositories_detail_user);
        tvFollowers = findViewById(R.id.tv_follower_detail_user);
        tvFollowing = findViewById(R.id.tv_following_detail_user);
        tvLocation = findViewById(R.id.tv_location_detail_user);
        tvCompany = findViewById(R.id.tv_company_detail_user);
        tvBio = findViewById(R.id.tv_bio_detail_user);
        vpFollowingFollowers = findViewById(R.id.vp_followers_following);
        tlFollowersFollowing = findViewById(R.id.tabLayout);
        fabFavoriteUser = findViewById(R.id.fab_favorite_user);

        detailUserViewModel.init(USER_NAME);
        fabFavoriteUser.setOnClickListener(v -> {
            if (detailUserViewModel.isFavoriteUser().getValue()){
                Log.d(TAG, "onCreate: delete " + detailUserViewModel.isFavoriteUser().getValue());
                detailUserViewModel.setFavoriteUser(false);
            }
            else {
                Log.d(TAG, "onCreate: no delete " + detailUserViewModel.isFavoriteUser().getValue());
                detailUserViewModel.setFavoriteUser(true);
            }
        });

        detailUserViewModel.loadDetailUser(USER_NAME);
        detailUserViewModel.getDetailUserResponse().observe(this, detailUserResponse -> {
            Glide.with(DetailUserActivity.this)
                    .load(detailUserResponse.getAvatarUrl())
                    .into(civUser);
            tvName.setText(detailUserResponse.getLogin());
            tvBio.setText(detailUserResponse.getBio());
            tvRepository.setText(String.valueOf(detailUserResponse.getPublicRepos()));
            tvFollowers.setText(String.valueOf(detailUserResponse.getFollowers()));
            tvFollowing.setText(String.valueOf(detailUserResponse.getFollowing()));
            tvLocation.setText(detailUserResponse.getLocation());
            tvCompany.setText(detailUserResponse.getCompany());
        });

        detailUserViewModel.isFavoriteUser().observe(this, aBoolean -> {
            if (aBoolean){
                fabFavoriteUser.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_color_24));
            }
            else {
                fabFavoriteUser.setImageDrawable(getDrawable(R.drawable.ic_baseline_favorite_border_24));
            }
        });

        vpAdapter = new ViewPagerAdapter(this, this, NAME_PAGES);
        vpFollowingFollowers.setAdapter(vpAdapter);

        new TabLayoutMediator(tlFollowersFollowing, vpFollowingFollowers, (tab, position) -> tab.setText(getString(NAME_PAGES[position]))).attach();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public static class FollowingFollowersFragment extends Fragment{
        RecyclerView rvFollowingFollowers;
        UserListAdapter userListAdapter;
        String CURRENT_PAGE;

        public FollowingFollowersFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_list_user, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            if (getArguments() != null) {
                CURRENT_PAGE = getArguments().getString("PAGE");
            }

            rvFollowingFollowers = view.findViewById(R.id.rv_user);
            userListAdapter = new UserListAdapter(getContext(), getClass().getSimpleName());

            rvFollowingFollowers.setLayoutManager(new LinearLayoutManager(getContext()));
            rvFollowingFollowers.setAdapter(userListAdapter);
            rvFollowingFollowers.setHasFixedSize(true);

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if(CURRENT_PAGE.equals(getResources().getString(R.string.user_Followers))){
                detailUserViewModel.getFollowersResponse().observe(requireActivity(), itemsItems -> {
                    if (itemsItems.size() != 0){
                        userListAdapter.setListUser(itemsItems);
                        userListAdapter.notifyDataSetChanged();
                    }
                });
            }
            else if(CURRENT_PAGE.equals(getResources().getString(R.string.user_following))){
                detailUserViewModel.getFollowingResponse().observe(requireActivity(), itemsItems -> {
                    if (itemsItems.size() != 0){
                        userListAdapter.setListUser(itemsItems);
                        userListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        int[] PAGES;
        Context context;
        public ViewPagerAdapter(Context context, @NonNull FragmentActivity fragmentActivity, int[] PAGES) {
            super(fragmentActivity);
            this.context = context;
            this.PAGES = PAGES;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new FollowingFollowersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("PAGE", context.getString(PAGES[position]));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return PAGES.length;
        }
    }
}