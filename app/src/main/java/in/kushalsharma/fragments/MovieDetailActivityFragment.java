package in.kushalsharma.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.kushalsharma.adapters.MovieDetailsAdapter;
import in.kushalsharma.cinephile.R;
import in.kushalsharma.models.MovieDetails;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.ContentProviderHelperMethods;
import in.kushalsharma.utils.DatabaseHelper;
import in.kushalsharma.utils.MoviesContentProvider;
import in.kushalsharma.utils.PaletteNetworkImageView;
import in.kushalsharma.utils.TmdbUrls;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

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

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        id = getActivity().getIntent().getStringExtra("id");
        movie = new MovieDetails();
        getMovieDataFromID(id);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_movie_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_movie_details);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mBackdrop = (PaletteNetworkImageView) v.findViewById(R.id.backdrop);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);


        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

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

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
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
                    String[] data = trailerInfo.get(0).split(",,");
                    startActivity(Intent.createChooser(shareIntent(TmdbUrls.YOUTUBE_URL + data[0]), "Share Via"));
                    return true;
                }
                return true;
            }
        });

        return v;
    }


    public void updateContent(String id) {
        getMovieDataFromID(id);
    }

    /**
     * `Method to Get Data From given ID
     *
     * @param id ID of Movie
     */

    private void getMovieDataFromID(final String id) {
        String url = TmdbUrls.MOVIE_BASE_URL + id + "?" + TmdbUrls.API_KEY;
        JsonObjectRequest getDetails = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    movie.setId(Integer.valueOf(id));
                    movie.setTitle(response.getString("title"));
                    movie.setRating(String.valueOf(response.getDouble("vote_average")));
                    String genres = "";
                    JSONArray genreArray = response.getJSONArray("genres");
                    for (int i = 0; i < genreArray.length(); i++) {
                        String genre = genreArray.getJSONObject(i).getString("name");
                        if (i != genreArray.length() - 1)
                            genres += genre + ", ";
                        else
                            genres += genre + ".";
                    }
                    movie.setGenre(genres);
                    movie.setDate(response.getString("release_date"));
                    movie.setStatus(response.getString("status"));
                    movie.setOverview(response.getString("overview"));
                    movie.setBackdrop("http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path"));
                    movie.setVoteCount(String.valueOf(response.getInt("vote_count")));
                    movie.setTagLine(response.getString("tagline"));
                    movie.setRuntime(String.valueOf(response.getInt("runtime")));
                    movie.setLanguage(response.getString("original_language"));
                    movie.setPopularity(String.valueOf(response.getDouble("popularity")));
                    movie.setPoster("http://image.tmdb.org/t/p/w342/" + response.getString("poster_path"));

                    mBackdrop.setImageUrl(movie.getBackdrop(), AppController.getInstance().getImageLoader());
                    mCollapsingToolbarLayout.setTitle(movie.getTitle());

                    mAdapter = new MovieDetailsAdapter(movie, trailerInfo, reviewInfo, getActivity());
                    mRecyclerView.setAdapter(mAdapter);

                    boolean isMovieInDB = ContentProviderHelperMethods
                            .isMovieInDatabase(getActivity(),
                                    String.valueOf(movie.getId()));
                    if (isMovieInDB) {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_outline));
                    }

                    fab.show();
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            boolean isMovieInDB = ContentProviderHelperMethods
                                    .isMovieInDatabase(getActivity(),
                                            String.valueOf(movie.getId()));
                            if (isMovieInDB) {
                                Snackbar.make(view, getResources().getString(R.string.already_in_favourites), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

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
                                        .setAction("Action", null).show();

                                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                            }
                        }
                    });

                    getTrailerInfo(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackBar(getString(R.string.error_msg));
            }
        });
        AppController.getInstance().addToRequestQueue(getDetails);
    }


    private void getTrailerInfo(final String id) {
        trailerInfo.clear();
        String requestUrl = TmdbUrls.MOVIE_BASE_URL + id + "/videos?" + TmdbUrls.API_KEY;

        JsonObjectRequest mTrailerRequest = new JsonObjectRequest(requestUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mTrailerArray = response.getJSONArray("results");
                    for (int i = 0; i < mTrailerArray.length(); i++) {
                        JSONObject mTrailerObject = mTrailerArray.getJSONObject(i);
                        trailerInfo.add(mTrailerObject.getString("key") + ",," + mTrailerObject.getString("name")
                                + ",," + mTrailerObject.getString("site") + ",," + mTrailerObject.getString("size"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    // Specify Adapter
                    mAdapter = new MovieDetailsAdapter(movie, trailerInfo, reviewInfo, getActivity());
                    mAdapter.notifyDataSetChanged();
                    getMovieReviews(id);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getMovieReviews(id);
            }
        });

        AppController.getInstance().addToRequestQueue(mTrailerRequest);
    }

    void getMovieReviews(String id) {
        reviewInfo.clear();
        String reviewUrl = TmdbUrls.MOVIE_BASE_URL + id + "/reviews?" + TmdbUrls.API_KEY;
        JsonObjectRequest mReviewRequest = new JsonObjectRequest(reviewUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    int size = response.getInt("total_results");
                    if (size != 0) {
                        JSONArray mReviewArray = response.getJSONArray("results");
                        for (int i = 0; i < mReviewArray.length(); i++) {
                            JSONObject mReview = mReviewArray.getJSONObject(i);
                            reviewInfo.add(mReview.getString("author") + "-" + mReview.getString("content"));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(mReviewRequest);
    }

    void showSnackBar(String msg) {
        Snackbar.make(mCollapsingToolbarLayout, msg, Snackbar.LENGTH_LONG)
                .show();
    }


    public Intent shareIntent(String data) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.movie_extra_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
        return sharingIntent;
    }
}
