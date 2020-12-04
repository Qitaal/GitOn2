package com.example.giton2.database;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.giton2.provider.AppContentProvider;

import java.lang.ref.WeakReference;
import java.util.List;

public class DatabaseRepository {
    private static final String TAG = "DatabaseRepository";
    private final FavoriteUserDAO favoriteUserDAO;
    private final DatabaseCallback databaseCallback;
    private final Context context;

    public DatabaseRepository(Application application, DatabaseCallback databaseCallback) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        favoriteUserDAO = appDatabase.favoriteUserDAO();
        this.databaseCallback = databaseCallback;
        this.context = application;
    }

    public void insert(FavoriteUser favoriteUser){
        new InsertAsync(favoriteUserDAO, databaseCallback).execute(favoriteUser);
    }

    public void delete(String name){
        new DeleteAsync(favoriteUserDAO, databaseCallback).execute(name);
    }

    public void isFavoriteUser (String name){
        new CheckIsFavoriteAsync(favoriteUserDAO, databaseCallback).execute(name);
    }

    public void getAll(){
        new GetAllAsync(favoriteUserDAO, databaseCallback).execute();
    }

    class GetAllAsync extends AsyncTask<Void, Void, List<FavoriteUser>>{
        private FavoriteUserDAO favoriteUserDAO;
        private WeakReference<DatabaseCallback> databseCallback;

        public GetAllAsync(FavoriteUserDAO favoriteUserDAO, DatabaseCallback databaseCallback) {
            this.favoriteUserDAO = favoriteUserDAO;
            this.databseCallback = new WeakReference<>(databaseCallback);
        }

        @Override
        protected List<FavoriteUser> doInBackground(Void... voids) {
//            Cursor cursor = favoriteUserDAO.selectAll();
            Cursor cursor = context.getContentResolver().query(AppContentProvider.URI_FAVORITE_USER, null, null, null, null);
            return MapingHelper.mapCursorToList(cursor);
        }

        @Override
        protected void onPostExecute(List<FavoriteUser> favoriteUsers) {
            super.onPostExecute(favoriteUsers);
            databseCallback.get().onSuccessRetrieve(favoriteUsers);
        }
    }

    class CheckIsFavoriteAsync extends AsyncTask<String, Void, Boolean>{
        private FavoriteUserDAO favoriteUserDAO;
        private WeakReference<DatabaseCallback> databaseCallback;

        public CheckIsFavoriteAsync(FavoriteUserDAO favoriteUserDAO, DatabaseCallback databaseCallback) {
            this.favoriteUserDAO = favoriteUserDAO;
            this.databaseCallback = new WeakReference<>(databaseCallback);
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            return favoriteUserDAO.isFavoriteUser(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            databaseCallback.get().onCheckedIsFavorited(aBoolean);
        }
    }

    class InsertAsync extends AsyncTask<FavoriteUser, Void, Void>{

        private FavoriteUserDAO favoriteUserDAO;
        private WeakReference<DatabaseCallback> databaseCallback;

        public InsertAsync(FavoriteUserDAO favoriteUserDAO, DatabaseCallback databaseCallback) {
            this.favoriteUserDAO = favoriteUserDAO;
            this.databaseCallback = new WeakReference<>(databaseCallback);
        }

        @Override
        protected Void doInBackground(FavoriteUser... favoriteUsers) {
            Log.d(TAG, "doInBackground: no delete " + favoriteUsers[0].getName());
            favoriteUserDAO.insert(favoriteUsers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            databaseCallback.get().onSuccessInsert();
        }
    }

    class DeleteAsync extends AsyncTask<String, Void, Void>{

        private FavoriteUserDAO favoriteUserDAO;
        private WeakReference<DatabaseCallback> databaseCallback;

        public DeleteAsync(FavoriteUserDAO favoriteUserDAO, DatabaseCallback databaseCallback) {
            this.favoriteUserDAO = favoriteUserDAO;
            this.databaseCallback = new WeakReference<>(databaseCallback);
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: delete " + strings[0]);
            favoriteUserDAO.delete(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            databaseCallback.get().onSuccessDelete();
        }
    }
}
