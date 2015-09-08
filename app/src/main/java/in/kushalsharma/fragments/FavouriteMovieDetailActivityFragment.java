package in.kushalsharma.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

import in.kushalsharma.adapters.MovieDetailsAdapter;
import in.kushalsharma.cinephile.R;
import in.kushalsharma.models.MovieDetails;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.ContentProviderHelperMethods;
import in.kushalsharma.utils.DatabaseHelper;
import in.kushalsharma.utils.MoviesContentProvider;
import in.kushalsharma.utils.PaletteNetworkImageView;


/**
 * A placeholder fragment containing a simple view.
 */
public class FavouriteMovieDetailActivityFragment extends Fragment {

    private String id;
    private MovieDetails movie;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private PaletteNetworkImageView mBackdrop;

    private ArrayList<String> trailerInfo = new ArrayList<>();
    private ArrayList<String> reviewInfo = new ArrayList<>();

    private FloatingActionButton fab;

    public FavouriteMovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite_movie_detail, container, false);


        id = getActivity().getIntent().getStringExtra("id");
        movie = null;

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_movie_details);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mBackdrop = (PaletteNetworkImageView) v.findViewById(R.id.backdrop);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);


        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        getMovieDataFromID(id);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mBackdrop.setResponseObserver(new PaletteNetworkImageView.ResponseObserver() {
            @Override
            public void onSuccess() {
                int colorDark = mBackdrop.getDarkVibrantColor();
                int colorLight = mBackdrop.getVibrantColor();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getActivity().getWindow().setStatusBarColor(colorDark);
                    fab.setBackgroundTintList(ColorStateList.valueOf(colorDark));
                }
                mCollapsingToolbarLayout.setContentScrimColor(colorLight);
            }

            @Override
            public void onError() {

            }
        });

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mToolbar.inflateMenu(R.menu.menu_movie_detail);


        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_share) {
                    startActivity(Intent.createChooser(shareIntent(movie.getTitle()), getResources().getString(R.string.share_via)));
                    return true;
                }
                return true;
            }
        });

        return v;
    }

    /**
     * Method to Get Data From given ID
     *
     * @param id ID of Movie
     */

    private void getMovieDataFromID(final String id) {
        movie = ContentProviderHelperMethods.getMovieFromDatabase(getActivity(), id);
        mBackdrop.setImageUrl(movie.getBackdrop(), AppController.getInstance().getImageLoader());
        mCollapsingToolbarLayout.setTitle(movie.getTitle());

        mAdapter = new MovieDetailsAdapter(movie, trailerInfo, reviewInfo, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        boolean isMovieInDB = ContentProviderHelperMethods
                .isMovieInDatabase(getActivity(),
                        String.valueOf(movie.getId()));
        if (isMovieInDB) {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isMovieInDB = ContentProviderHelperMethods
                        .isMovieInDatabase(getActivity(), String.valueOf(movie.getId()));
                if (isMovieInDB) {

                    Uri contentUri = MoviesContentProvider.CONTENT_URI;
                    getActivity().getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(movie.getId())});
                    Snackbar.make(view, getResources().getString(R.string.removed_from_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));

                } else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.KEY_ID, movie.getId());
                    values.put(DatabaseHelper.KEY_TITLE, movie.getTitle());
                    values.put(DatabaseHelper.KEY_RATING, movie.getRating());
                    values.put(DatabaseHelper.KEY_GENRE, movie.getGenre());
                    values.put(DatabaseHelper.KEY_DATE, movie.getDate());
                    values.put(DatabaseHelper.KEY_STATUS, movie.getStatus());
                    values.put(DatabaseHelper.KEY_OVERVIEW, movie.getOverview());
                    values.put(DatabaseHelper.KEY_BACKDROP, movie.getBackdrop());
                    values.put(DatabaseHelper.KEY_VOTE_COUNT, movie.getVoteCount());
                    values.put(DatabaseHelper.KEY_TAG_LINE, movie.getTagLine());
                    values.put(DatabaseHelper.KEY_RUN_TIME, movie.getRuntime());
                    values.put(DatabaseHelper.KEY_LANGUAGE, movie.getLanguage());
                    values.put(DatabaseHelper.KEY_POPULARITY, movie.getPopularity());
                    values.put(DatabaseHelper.KEY_POSTER, movie.getPoster());

                    getActivity().getContentResolver().insert(MoviesContentProvider.CONTENT_URI, values);

                    Snackbar.make(view, getResources().getString(R.string.added_to_favourites), Snackbar.LENGTH_LONG)
                            .show();

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
                }
            }
        });

    }


    public Intent shareIntent(String data) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.movie_extra_subject));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
        return sharingIntent;
    }
}
