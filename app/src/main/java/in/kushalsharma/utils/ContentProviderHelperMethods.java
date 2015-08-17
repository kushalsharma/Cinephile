package in.kushalsharma.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import in.kushalsharma.models.Movie;
import in.kushalsharma.models.MovieDetails;

/**
 * Created by kushal on 17/08/15.
 */
public class ContentProviderHelperMethods {

    public static ArrayList<Movie> getMovieListFromDatabase(Activity mAct) {

        ArrayList<Movie> mMovieList = new ArrayList<>();
        Uri contentUri = MoviesContentProvider.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {

                Movie movie = new Movie(c.getString(c.getColumnIndex(DatabaseHelper.KEY_TITLE)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_POSTER)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_DATE)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_OVERVIEW)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_ID)));

                mMovieList.add(movie);
            } while (c.moveToNext());
        }
        c.close();
        return mMovieList;
    }

    public static boolean isMovieInDatabase(Activity mAct, String id) {

        ArrayList<Movie> list = new ArrayList<>(ContentProviderHelperMethods
                .getMovieListFromDatabase(mAct));
        for (Movie listItem : list) {
            if (listItem.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static MovieDetails getMovieFromDatabase(Activity mAct, String ID) {
        MovieDetails movie = null;
        Uri contentUri = MoviesContentProvider.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                if (ID.equals(c.getString(c.getColumnIndex(DatabaseHelper.KEY_ID)))) {
                    movie = new MovieDetails(Integer.valueOf(c.getString(c.getColumnIndex(DatabaseHelper.KEY_ID))),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_TITLE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_RATING)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_GENRE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_DATE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_STATUS)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_OVERVIEW)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_BACKDROP)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_VOTE_COUNT)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_TAG_LINE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_RUN_TIME)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_LANGUAGE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_POPULARITY)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_POSTER)));
                    break;
                }

            } while (c.moveToNext());
        }
        c.close();
        return movie;
    }
}
