package com.example.consumerapp;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MapingHelper {
    private static final String TAG = "MapingHelper";
    public static List<FavoriteUser> mapCursorToList(Cursor cursor) {
        ArrayList<FavoriteUser> favoriteUsers = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteUser._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteUser.COLUMN_NAME));
            String avatar_url = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteUser.COLUMN_AVATAR_URL));
            favoriteUsers.add(new FavoriteUser(id, name, avatar_url));
            Log.d(TAG, "mapCursorToList: " + name);
        }
        return favoriteUsers;
    }
}
