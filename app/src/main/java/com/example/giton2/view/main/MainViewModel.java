package com.example.giton2.view.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.giton2.model.search.ItemsItem;
import com.example.giton2.model.search.SearchResponse;
import com.example.giton2.service.APIService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<ItemsItem>> userList;

    public MutableLiveData<List<ItemsItem>> getUserList() {
        return userList;
    }

    public MainViewModel() {
        userList = new MutableLiveData<>();
    }

    public void searchUsers(String q){
        APIService apiService = new APIService();
        apiService.getAPI().getSearch(q).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.body() != null && response.body().getTotalCount() != 0) {
                    userList.setValue(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                userList.setValue(null);
            }
        });
    }
}
