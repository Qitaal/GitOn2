package com.example.giton2.view.favorite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.giton2.R;
import com.example.giton2.adapter.UserListAdapter;

public class FavoriteFragment extends Fragment {
    RecyclerView rvFavoriteUser;
    UserListAdapter userListAdapter;
    ProgressBar progressBar;
    FavoriteUserViewModel favoriteUserViewModel;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteUserViewModel = new ViewModelProvider(requireActivity()).get(FavoriteUserViewModel.class);
        userListAdapter = new UserListAdapter(getContext(), getClass().getSimpleName());
        progressBar = view.findViewById(R.id.pb_search);
        rvFavoriteUser = view.findViewById(R.id.rv_user);

        progressBar.setVisibility(View.INVISIBLE);
        rvFavoriteUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavoriteUser.setAdapter(userListAdapter);
        rvFavoriteUser.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoriteUserViewModel.getListLiveData().observe(requireActivity(), favoriteUsers -> {
            userListAdapter.setFavoriteUserList(favoriteUsers);
            userListAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteUserViewModel.refreshData();
    }
}