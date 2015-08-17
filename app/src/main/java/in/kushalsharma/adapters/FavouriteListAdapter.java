package in.kushalsharma.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import in.kushalsharma.cinephile.FavouriteMovieDetailActivity;
import in.kushalsharma.cinephile.MovieDetailActivity;
import in.kushalsharma.cinephile.R;
import in.kushalsharma.models.Movie;
import in.kushalsharma.utils.AppController;
import in.kushalsharma.utils.Typefaces;

public class FavouriteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private Activity mAct;
    private LayoutInflater mInflater;

    public FavouriteListAdapter(ArrayList<Movie> mMovieList, Activity activity) {
        this.mMovieList = mMovieList;
        this.mAct = activity;

        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == 2) {
            View v = mInflater.inflate(R.layout.layout_holder_movie_small, parent, false);
            vh = new ViewHolderSmall(v);
        } else {
            View v = mInflater.inflate(R.layout.layout_holder_movie_large, parent, false);
            vh = new ViewHolderLarge(v);
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 4 == 0)
            return 1;
        else
            return 2;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 2:
                ((ViewHolderSmall) holder).getImageView().setImageUrl(mMovieList.get(position).getImage(), AppController.getInstance().getImageLoader());
                ((ViewHolderSmall) holder).getTitleView().setText(mMovieList.get(position).getTitle());
                ((ViewHolderSmall) holder).getTitleView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));

//                ((ViewHolderSmall) holder).getToolbar().getMenu().clear();
//                ((ViewHolderSmall) holder).getToolbar().inflateMenu(R.menu.card_toolbar_menu);

                ((ViewHolderSmall) holder).getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mMovieDetailIntent = new Intent(mAct, FavouriteMovieDetailActivity.class);
                        mMovieDetailIntent.putExtra("id", mMovieList.get(position).getId());
                        mAct.startActivity(mMovieDetailIntent);
                    }
                });

                break;
            case 1:
                ((ViewHolderLarge) holder).getImageView().setImageUrl(mMovieList.get(position).getImage(), AppController.getInstance().getImageLoader());
                ((ViewHolderLarge) holder).getTitleView().setText(mMovieList.get(position).getTitle());
                ((ViewHolderLarge) holder).getOverviewView().setText(mMovieList.get(position).getOverview());
                ((ViewHolderLarge) holder).getTitleView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderLarge) holder).getOverviewView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderLarge) holder).getReadMoreView().setTypeface(Typefaces.get(mAct, "RobotoSlab-Regular.ttf"));
                ((ViewHolderLarge) holder).getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mMovieDetailIntent = new Intent(mAct, FavouriteMovieDetailActivity.class);
                        mMovieDetailIntent.putExtra("id", mMovieList.get(position).getId());
                        mAct.startActivity(mMovieDetailIntent);
                    }
                });
                ((ViewHolderLarge) holder).getReadMoreView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mMovieDetailIntent = new Intent(mAct, FavouriteMovieDetailActivity.class);
                        mMovieDetailIntent.putExtra("id", mMovieList.get(position).getId());
                        mAct.startActivity(mMovieDetailIntent);
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public static class ViewHolderSmall extends RecyclerView.ViewHolder {

        private NetworkImageView imageView;
        private TextView titleView;
//        private Toolbar toolbar;

        public ViewHolderSmall(View v) {
            super(v);
            imageView = (NetworkImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
//            toolbar = (Toolbar) v.findViewById(R.id.card_toolbar);
        }

        public NetworkImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

//        public Toolbar getToolbar() {
//            return toolbar;
//        }
    }

    public static class ViewHolderLarge extends RecyclerView.ViewHolder {

        private NetworkImageView imageView;
        private TextView titleView, overviewView, readMoreView;

        public ViewHolderLarge(View v) {
            super(v);
            imageView = (NetworkImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
            overviewView = (TextView) v.findViewById(R.id.overview);
            readMoreView = (TextView) v.findViewById(R.id.read_more);
        }

        public NetworkImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getOverviewView() {
            return overviewView;
        }

        public TextView getReadMoreView() {
            return readMoreView;
        }
    }
}
