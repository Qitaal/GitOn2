package com.example.consumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private UserListAdapter userListAdapter;
    public static final String AUTHORITY = "com.example.giton2";
    public static final String TABLE_NAME = "favorite_user";
    public static final Uri URI_FAVORITE_USER = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME + "/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvUserList = findViewById(R.id.rv_main);
        userListAdapter = new UserListAdapter(this);

        rvUserList.setAdapter(userListAdapter);
        rvUserList.setHasFixedSize(true);
        rvUserList.setLayoutManager(new LinearLayoutManager(this));

        new LoadUserAsync(getApplicationContext(), contentResolverCallback).execute();
    }
    
    static class LoadUserAsync extends AsyncTask<Void, Void, List<FavoriteUser>>{
        WeakReference<Context> context;
        WeakReference<ContentResolverCallback> callback;

        public LoadUserAsync(Context context, ContentResolverCallback contentResolverCallback) {
            this.context = new WeakReference<>(context);
            this.callback = new WeakReference<>(contentResolverCallback);
        }

        @Override
        protected List<FavoriteUser> doInBackground(Void... voids) {
            List<FavoriteUser> favoriteUsers = new ArrayList<>();
            ContentResolver contentResolver = context.get().getContentResolver();
            if (contentResolver != null) {
                Log.d(TAG, "doInBackground: CONTENT != NULL");
                Cursor cursor = contentResolver.query(URI_FAVORITE_USER,
                        null, null, null, null);
                if (cursor != null) {
                    Log.d(TAG, "doInBackground: CURSOR != NULL");
                    favoriteUsers = MapingHelper.mapCursorToList(cursor);
                }else{
                    Log.d(TAG, "doInBackground: CURSOR == NULL");
                }
            } else {
                Log.d(TAG, "doInBackground: CONTENT == NULL");
            }
            return favoriteUsers;
        }

        @Override
        protected void onPostExecute(List<FavoriteUser> favoriteUsers) {
            super.onPostExecute(favoriteUsers);
            callback.get().onSuccess(favoriteUsers);
        }
    }

    ContentResolverCallback contentResolverCallback = new ContentResolverCallback() {
        @Override
        public void onSuccess(List<FavoriteUser> favoriteUsers) {
            userListAdapter.setFavoriteUsers(favoriteUsers);
            userListAdapter.notifyDataSetChanged();
        }
    };
}
interface ContentResolverCallback{
    void onSuccess(List<FavoriteUser> favoriteUsers);
}