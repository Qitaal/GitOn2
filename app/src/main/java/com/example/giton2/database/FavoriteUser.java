package com.example.giton2.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;

import static com.example.giton2.database.FavoriteUser.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class FavoriteUser implements BaseColumns{

    public static final String TABLE_NAME = "favorite_user";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AVATAR_URL = "avatar_url";

    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @ColumnInfo(name = COLUMN_AVATAR_URL)
    private String avatar_url;

    public FavoriteUser(int id, String name, String avatar_url) {
        this.id = id;
        this.name = name;
        this.avatar_url = avatar_url;
    }
    public FavoriteUser(){

    }

    @NonNull
    public static FavoriteUser fromContentValues(@Nullable ContentValues values){
        final FavoriteUser favoriteUser = new FavoriteUser();

        if (values != null){
            if (values.containsKey(COLUMN_ID)){
                favoriteUser.id = values.getAsInteger(COLUMN_ID);
            }
            if (values.containsKey(COLUMN_NAME)) {
                favoriteUser.name = values.getAsString(COLUMN_NAME);
            }
            if (values.containsKey(COLUMN_AVATAR_URL)){
                favoriteUser.avatar_url = values.getAsString(COLUMN_AVATAR_URL);
            }
        }
        return favoriteUser;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

}
