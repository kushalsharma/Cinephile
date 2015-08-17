package in.kushalsharma.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

import in.kushalsharma.cinephile.R;
import in.kushalsharma.models.MovieDetails;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.PaletteNetworkImageView;
import in.kushalsharma.utils.TmdbUrls;
import in.kushalsharma.utils.Typefaces;

/**
 * Created by kushal on 04/08/15.
 * Movie details adapter
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MovieDetails movie;
    private Activity mAct;
    private LayoutInflater mInflater;
    private ArrayList<String> trailerInfo;
    private ArrayList<String> reviewInfo;

    public MovieDetailsAdapter(MovieDetails movie, ArrayList<String> trailerInfo, ArrayList<String> reviewInfo, Activity mActivity) {
        this.movie = movie;
        this.trailerInfo = trailerInfo;
        this.reviewInfo = reviewInfo;
        this.mAct = mActivity;

        mInflater = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == 0) {
            View v = mInflater.inflate(R.layout.layout_holder_details, parent, false);
            vh = new ViewHolderDetails(v);
            return vh;
        }

        if (viewType == 1) {
            View v = mInflater.inflate(R.layout.layout_holder_trailer, parent, false);
            vh = new ViewHolderTrailer(v);
            return vh;
        }

        if (viewType == 2) {
            View v = mInflater.inflate(R.layout.layout_holder_review, parent, false);
            vh = new ViewHolderReview(v);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case 0:
                ((ViewHolderDetails) holder).getImageView().setImageUrl(movie.getPoster(),
                        AppController.getInstance().getImageLoader());
                ((ViewHolderDetails) holder).getTitleView().setText(movie.getTitle());
                if (!movie.getTagLine().equals("")) {
                    ((ViewHolderDetails) holder).getTaglineView().setText("\"" + movie.getTagLine() + "\"");
                } else {
                    ((ViewHolderDetails) holder).getTaglineView().setVisibility(View.GONE);
                }
                ((ViewHolderDetails) holder).getDateStatusView().setText(movie.getDate()
                        + " (" + movie.getStatus() + ")");
                ((ViewHolderDetails) holder).getDurationView().setText(mAct.getString(R.string.duration)
                        + movie.getRuntime() + mAct.getString(R.string.min));
                ((ViewHolderDetails) holder).getRatingView().setText(movie.getRating());
                try {
                    ((ViewHolderDetails) holder).getGenreView().setText(movie.getGenre().substring(0,
                            movie.getGenre().indexOf(",")));
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    ((ViewHolderDetails) holder).getGenreView().setText(movie.getGenre().substring(0,
                            movie.getGenre().indexOf(".")));
                }
                ((ViewHolderDetails) holder).getPopularityView().setText(movie.getPopularity().substring(0, 4));
                ((ViewHolderDetails) holder).getLanguageView().setText(movie.getLanguage());
                ((ViewHolderDetails) holder).getOverviewView().setText(movie.getOverview());
                ((ViewHolderDetails) holder).getVoteCountView().setText(movie.getVoteCount() + " votes");

                ((ViewHolderDetails) holder).getTitleView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getTaglineView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getDateStatusView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getDurationView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getRatingView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getGenreView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getPopularityView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getLanguageView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderDetails) holder).getOverviewView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));

                ((ViewHolderDetails) holder).getImageView().setResponseObserver(new PaletteNetworkImageView.ResponseObserver() {
                    @Override
                    public void onSuccess() {
                        int color = ((ViewHolderDetails) holder).getImageView().getDarkVibrantColor();

                        ((ViewHolderDetails) holder).getRatingsBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        ((ViewHolderDetails) holder).getGenreBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        ((ViewHolderDetails) holder).getPopBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        ((ViewHolderDetails) holder).getLangBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                    }

                    @Override
                    public void onError() {

                    }
                });


                break;
            case 1:
                final String[] data = trailerInfo.get(position - 1).split(",,");
                ((ViewHolderTrailer) holder).getImageView().setImageUrl(TmdbUrls.YOUTUBE_THUMB + data[0] + TmdbUrls.YOUTUBE_MEDIUM_QUALITY, AppController.getInstance().getImageLoader());
                ((ViewHolderTrailer) holder).getTitleView().setText(data[1]);
                ((ViewHolderTrailer) holder).getSiteView().setText(mAct.getString(R.string.site) + data[2]);
                ((ViewHolderTrailer) holder).getQualityView().setText(mAct.getString(R.string.quality) + data[3] + "p");
                ((ViewHolderTrailer) holder).getTitleView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderTrailer) holder).getSiteView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderTrailer) holder).getQualityView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));


                ((ViewHolderTrailer) holder).getRippleLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAct.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TmdbUrls.YOUTUBE_URL + data[0])));
                    }
                });
                break;
            case 2:
                ((ViewHolderReview) holder).getReviewView().setText(reviewInfo.get(position - 1 - trailerInfo.size())
                        .substring(reviewInfo.get(position - 1 - trailerInfo.size()).indexOf("-") + 1));
                ((ViewHolderReview) holder).getReviewAuthorView().setText(reviewInfo.get(position - 1 - trailerInfo.size())
                        .substring(0, reviewInfo.get(position - 1 - trailerInfo.size()).indexOf("-")));
                ((ViewHolderReview) holder).getReviewAuthorView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderReview) holder).getReviewView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + trailerInfo.size() + reviewInfo.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return 0;
        if (position > 0 && position <= trailerInfo.size())
            return 1;
        if (position > trailerInfo.size() && position <= trailerInfo.size() + reviewInfo.size())
            return 2;
        return 666;
    }

    public static class ViewHolderDetails extends RecyclerView.ViewHolder {

        private PaletteNetworkImageView imageView;
        private TextView titleView, taglineView, dateStatusView, durationView,
                ratingView, genreView, popularityView, languageView, overviewView, voteCountView;
        private ImageView ratingsBackground, genreBackground, popBackground, langBackground;

        public ViewHolderDetails(View v) {
            super(v);
            imageView = (PaletteNetworkImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
            taglineView = (TextView) v.findViewById(R.id.tagline);
            dateStatusView = (TextView) v.findViewById(R.id.date_status);
            durationView = (TextView) v.findViewById(R.id.duration);
            ratingView = (TextView) v.findViewById(R.id.rating);
            genreView = (TextView) v.findViewById(R.id.genre);
            popularityView = (TextView) v.findViewById(R.id.popularity);
            languageView = (TextView) v.findViewById(R.id.language);
            overviewView = (TextView) v.findViewById(R.id.overview);
            ratingsBackground = (ImageView) v.findViewById(R.id.ratings_background);
            voteCountView = (TextView) v.findViewById(R.id.vote_count);
            genreBackground = (ImageView) v.findViewById(R.id.genre_background);
            popBackground = (ImageView) v.findViewById(R.id.pop_background);
            langBackground = (ImageView) v.findViewById(R.id.lang_background);
        }

        public PaletteNetworkImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getTaglineView() {
            return taglineView;
        }

        public TextView getDateStatusView() {
            return dateStatusView;
        }

        public TextView getDurationView() {
            return durationView;
        }

        public TextView getRatingView() {
            return ratingView;
        }

        public TextView getGenreView() {
            return genreView;
        }

        public TextView getPopularityView() {
            return popularityView;
        }

        public TextView getLanguageView() {
            return languageView;
        }

        public TextView getOverviewView() {
            return overviewView;
        }

        public ImageView getRatingsBackground() {
            return ratingsBackground;
        }

        public TextView getVoteCountView() {
            return voteCountView;
        }

        public ImageView getGenreBackground() {
            return genreBackground;
        }

        public ImageView getPopBackground() {
            return popBackground;
        }

        public ImageView getLangBackground() {
            return langBackground;
        }
    }

    public static class ViewHolderTrailer extends RecyclerView.ViewHolder {

        private NetworkImageView imageView;
        private MaterialRippleLayout rippleLayout;
        private TextView titleView, siteView, qualityView;

        public ViewHolderTrailer(View itemView) {
            super(itemView);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            imageView = (NetworkImageView) itemView.findViewById(R.id.trailer_image);
            titleView = (TextView) itemView.findViewById(R.id.title_text);
            siteView = (TextView) itemView.findViewById(R.id.site_text);
            qualityView = (TextView) itemView.findViewById(R.id.quality_text);
        }

        public MaterialRippleLayout getRippleLayout() {
            return rippleLayout;
        }

        public NetworkImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getSiteView() {
            return siteView;
        }

        public TextView getQualityView() {
            return qualityView;
        }
    }

    public static class ViewHolderReview extends RecyclerView.ViewHolder {

        private TextView reviewView;
        private TextView reviewAuthorView;

        public ViewHolderReview(View itemView) {
            super(itemView);
            reviewAuthorView = (TextView) itemView.findViewById(R.id.review_author_text);
            reviewView = (TextView) itemView.findViewById(R.id.review_text);
        }

        public TextView getReviewAuthorView() {
            return reviewAuthorView;
        }

        public TextView getReviewView() {
            return reviewView;
        }
    }
}
