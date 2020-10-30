package com.example.giton2.view.detailuser;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.giton2.model.detailuser.DetailUserResponse;
import com.example.giton2.model.search.ItemsItem;
import com.example.giton2.service.APIService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserViewModel extends ViewModel {
    MutableLiveData<DetailUserResponse> detailUserResponse;
    MutableLiveData<List<ItemsItem>> followingResponse;
    MutableLiveData<List<ItemsItem>> followersResponse;

    public MutableLiveData<List<ItemsItem>> getFollowersResponse() {
        return followersResponse;
    }

    public MutableLiveData<List<ItemsItem>> getFollowingResponse() {
        return followingResponse;
    }

    public MutableLiveData<DetailUserResponse> getDetailUserResponse() {
        return detailUserResponse;
    }

    public DetailUserViewModel() {
        detailUserResponse = new MutableLiveData<>();
        followingResponse = new MutableLiveData<>();
        followersResponse = new MutableLiveData<>();
    }

    public void loadDetailUser(String name){
        APIService apiService = new APIService();
        apiService.getAPI().getDetailUser(name).enqueue(new Callback<DetailUserResponse>() {
            @Override
            public void onResponse(Call<DetailUserResponse> call, Response<DetailUserResponse> response) {
                detailUserResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DetailUserResponse> call, Throwable t) {

            }
        });

        apiService.getAPI().getFollowing(name).enqueue(new Callback<List<ItemsItem>>() {
            @Override
            public void onResponse(Call<List<ItemsItem>> call, Response<List<ItemsItem>> response) {
                followingResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ItemsItem>> call, Throwable t) {

            }
        });

        apiService.getAPI().getFollowers(name).enqueue(new Callback<List<ItemsItem>>() {
            @Override
            public void onResponse(Call<List<ItemsItem>> call, Response<List<ItemsItem>> response) {
                followersResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ItemsItem>> call, Throwable t) {

            }
        });
    }
}
