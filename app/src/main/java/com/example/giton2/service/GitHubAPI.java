package com.example.giton2.service;

import com.example.giton2.model.detailuser.DetailUserResponse;
import com.example.giton2.model.search.ItemsItem;
import com.example.giton2.model.search.SearchResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubAPI {
    @GET("search/users")
    Call<SearchResponse> getSearch(@Query("q") String q);

    @GET("users/{user}")
    Call<DetailUserResponse> getDetailUser(@Path("user") String user);

    @GET("users/{user}/followers")
    Call<List<ItemsItem>> getFollowers(@Path("user") String user);

    @GET("users/{user}/following")
    Call<List<ItemsItem>> getFollowing(@Path("user") String user);
}
