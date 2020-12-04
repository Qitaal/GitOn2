package com.example.giton2.view.detailuser;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.giton2.database.DatabaseCallback;
import com.example.giton2.database.DatabaseRepository;
import com.example.giton2.database.FavoriteUser;
import com.example.giton2.model.detailuser.DetailUserResponse;
import com.example.giton2.model.search.ItemsItem;
import com.example.giton2.service.APIService;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserViewModel extends AndroidViewModel {
    private static final String TAG = "DetailUserViewModel";
    MutableLiveData<DetailUserResponse> detailUserResponse;
    MutableLiveData<List<ItemsItem>> followingResponse;
    MutableLiveData<List<ItemsItem>> followersResponse;
    MutableLiveData<Boolean> favoritedUser;
    DatabaseRepository databaseRepository;

    public DetailUserViewModel(@NonNull Application application) {
        super(application);
        detailUserResponse = new MutableLiveData<>();
        followingResponse = new MutableLiveData<>();
        followersResponse = new MutableLiveData<>();
        favoritedUser = new MutableLiveData<>();
        databaseRepository = new DatabaseRepository(application, databaseCallback);
    }

    public MutableLiveData<List<ItemsItem>> getFollowersResponse() {
        return followersResponse;
    }

    public MutableLiveData<List<ItemsItem>> getFollowingResponse() {
        return followingResponse;
    }

    public MutableLiveData<DetailUserResponse> getDetailUserResponse() {
        return detailUserResponse;
    }

    public MutableLiveData<Boolean> isFavoriteUser(){
        return favoritedUser;
    }

    void init(String name){
        databaseRepository.isFavoriteUser(name);
    }

    public void setFavoriteUser(boolean isfavoriteUser) {
        if (isfavoriteUser){
            FavoriteUser favoriteUser = new FavoriteUser(
                    detailUserResponse.getValue().getId(),
                    detailUserResponse.getValue().getLogin(),
                    detailUserResponse.getValue().getAvatarUrl()
            );
            databaseRepository.insert(favoriteUser);
        }
        else{
            databaseRepository.delete(detailUserResponse.getValue().getLogin());
        }
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

    DatabaseCallback databaseCallback = new DatabaseCallback() {
        @Override
        public void onSuccessInsert() {
            favoritedUser.setValue(true);
            Log.d(TAG, "onSuccessInsert: no delete");
        }

        @Override
        public void onSuccessDelete() {
            favoritedUser.setValue(false);
            Log.d(TAG, "onSuccessDelete: delete");
        }

        @Override
        public void onSuccessRetrieve(List<FavoriteUser> favoriteUsers) {

        }


        @Override
        public void onCheckedIsFavorited(boolean result) {
            favoritedUser.setValue(result);
        }
    };
}