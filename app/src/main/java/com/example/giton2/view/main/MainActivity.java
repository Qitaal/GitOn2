package com.example.giton2.view.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.giton2.R;
import com.example.giton2.adapter.UserListAdapter;
import com.example.giton2.model.search.ItemsItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    static MainViewModel mainViewModel;
    static ProgressBar pbSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.app_bar_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (!query.equals("")){
                        pbSearch.setVisibility(View.VISIBLE);
                        mainViewModel.searchUsers(query);
                    }
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.equals("")){
                        pbSearch.setVisibility(View.VISIBLE);
                        mainViewModel.searchUsers(newText);
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_setting){
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SearchFragment extends Fragment{
        RecyclerView rvUserMain;
        UserListAdapter userListAdapter;
//        List<ItemsItem> userList;

        public SearchFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_list_user, container, false);
        }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            userListAdapter = new UserListAdapter(getContext());
            pbSearch = view.findViewById(R.id.pb_search);
            rvUserMain = view.findViewById(R.id.rv_user);

            pbSearch.setVisibility(View.INVISIBLE);
            rvUserMain.setLayoutManager(new LinearLayoutManager(getContext()));
            rvUserMain.setAdapter(userListAdapter);
            rvUserMain.setHasFixedSize(true);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            mainViewModel.getUserList().observe(requireActivity(), itemsItems -> {
                userListAdapter.setListUser(itemsItems);
                userListAdapter.notifyDataSetChanged();
                pbSearch.setVisibility(View.INVISIBLE);
            });

        }
    }
}