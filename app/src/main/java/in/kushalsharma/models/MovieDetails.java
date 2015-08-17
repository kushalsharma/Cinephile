package in.kushalsharma.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kushal on 02/08/15.
 * Movie Details Class
 */

public class MovieDetails implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };
    private String title, rating, genre, date, status, overview, backdrop, voteCount, tagLine,
            runtime, language, popularity, poster;
    private int id;

    public MovieDetails() {
    }

    public MovieDetails(int id, String title, String rating, String genre, String date,
                        String status, String overview, String backdrop, String voteCount,
                        String tagLine, String runtime, String language, String popularity,
                        String poster) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.genre = genre;
        this.date = date;
        this.status = status;
        this.overview = overview;
        this.backdrop = backdrop;
        this.voteCount = voteCount;
        this.tagLine = tagLine;
        this.runtime = runtime;
        this.language = language;
        this.popularity = popularity;
        this.poster = poster;
    }

    public MovieDetails(Parcel in) {
        String[] data = new String[14];

        in.readStringArray(data);
        this.id = Integer.valueOf(data[0]);
        this.title = data[1];
        this.rating = data[2];
        this.genre = data[3];
        this.date = data[4];
        this.status = data[5];
        this.overview = data[6];
        this.backdrop = data[7];
        this.voteCount = data[8];
        this.tagLine = data[9];
        this.runtime = data[10];
        this.language = data[11];
        this.popularity = data[12];
        this.poster = data[13];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.id),
                this.title, this.rating, this.genre, this.date,
                this.status, this.overview, this.backdrop,
                this.voteCount, this.tagLine, this.runtime,
                this.language, this.popularity, this.poster});
    }
}
