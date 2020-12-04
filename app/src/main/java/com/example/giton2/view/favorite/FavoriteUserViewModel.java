package com.example.giton2.view.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.giton2.database.DatabaseCallback;
import com.example.giton2.database.DatabaseRepository;
import com.example.giton2.database.FavoriteUser;

import java.util.List;

public class FavoriteUserViewModel extends AndroidViewModel {
    DatabaseRepository databaseRepository;
    MutableLiveData<List<FavoriteUser>> listLiveData;
    public FavoriteUserViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application, databaseCallback);
        listLiveData = new MutableLiveData<>();
        databaseRepository.getAll();
    }

    public MutableLiveData<List<FavoriteUser>> getListLiveData() {
        return listLiveData;
    }

    public void refreshData(){
        databaseRepository.getAll();
    }

    DatabaseCallback databaseCallback = new DatabaseCallback() {
        @Override
        public void onSuccessInsert() {
        }

        @Override
        public void onSuccessDelete() {
        }

        @Override
        public void onSuccessRetrieve(List<FavoriteUser> favoriteUsers) {
            listLiveData.setValue(favoriteUsers);
        }

        @Override
        public void onCheckedIsFavorited(boolean result) {

        }

    };
}
