package com.example.giton2.database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface FavoriteUserDAO {

    @Insert
    void insert(FavoriteUser favoriteUser);

    @Query("DELETE FROM favorite_user WHERE name=:name")
    void delete(String name);

    @Query("SELECT * FROM favorite_user WHERE name LIKE :name")
    boolean isFavoriteUser(String name);

    @Query("SELECT * FROM favorite_user")
    Cursor selectAll();
}
