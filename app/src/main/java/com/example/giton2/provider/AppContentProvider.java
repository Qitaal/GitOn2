    package com.example.giton2.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.giton2.database.AppDatabase;
import com.example.giton2.database.FavoriteUser;
import com.example.giton2.database.FavoriteUserDAO;

public class AppContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.giton2";

    public static final Uri URI_FAVORITE_USER = Uri.parse("content://" + AUTHORITY + "/" + FavoriteUser.TABLE_NAME + "/");

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int CODE_FAVORITE_USER = 1;

    private FavoriteUserDAO favoriteUserDAO;

    static {
        MATCHER.addURI(AUTHORITY, FavoriteUser.TABLE_NAME, CODE_FAVORITE_USER);
    }

    public AppContentProvider() {
    }

    @Override
    public boolean onCreate() {
        favoriteUserDAO = AppDatabase.getInstance(getContext()).favoriteUserDAO();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_FAVORITE_USER){
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            final Cursor cursor;
            cursor = favoriteUserDAO.selectAll();
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        }
        else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        if (MATCHER.match(uri) == CODE_FAVORITE_USER) {
            return "vnd.android.cursor.dir/" + AUTHORITY + "." + FavoriteUser.TABLE_NAME;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}