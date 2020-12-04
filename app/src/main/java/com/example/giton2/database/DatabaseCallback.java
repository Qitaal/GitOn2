package com.example.giton2.database;

import java.util.List;

public interface DatabaseCallback {
    void onSuccessInsert();
    void onSuccessDelete();
    void onSuccessRetrieve(List<FavoriteUser> favoriteUsers);
    void onCheckedIsFavorited(boolean result);
}
