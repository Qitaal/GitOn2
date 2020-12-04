package com.example.giton2.view.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.giton2.R;
import com.example.giton2.adapter.UserListAdapter;

public class MainActivity extends AppCompatActivity{
    static MainViewModel mainViewModel;
    static ProgressBar pbSearch;

    private AppBarConfiguration appBarConfiguration;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.searchFragment, R.id.favoriteFragment).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favoriteFragment){
            NavigationUI.onNavDestinationSelected(item, navController);
        }
        else if (item.getItemId() == R.id.searchFragment){
            NavigationUI.onNavDestinationSelected(item, navController);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            if (searchManager != null) {
                SearchView searchView = (SearchView) item.getActionView();
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
        }
        else if (item.getItemId() == R.id.settingsFragment){
            NavigationUI.onNavDestinationSelected(item, navController);
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SearchFragment extends Fragment{
        private static final String TAG = "SearchFragment";
        RecyclerView rvUserMain;
        UserListAdapter userListAdapter;

        public SearchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_list_user, container, false);
        }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Log.d(TAG, "onViewCreated: " + getClass().getSimpleName());
            userListAdapter = new UserListAdapter(getContext(), getClass().getSimpleName());
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