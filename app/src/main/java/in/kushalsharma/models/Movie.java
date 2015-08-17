package in.kushalsharma.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kushal on 01/08/15.
 * Class for Movie
 */


public class Movie implements Parcelable {

    public static final Creator CREATOR = new Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String title, image, date, overview, id;

    public Movie(String title, String image, String date, String overview, String id) {
        this.title = title;
        this.image = image;
        this.date = date;
        this.overview = overview;
        this.id = id;
    }

    public Movie(Parcel in) {
        String[] data = new String[5];

        in.readStringArray(data);
        this.title = data[0];
        this.image = data[1];
        this.date = data[2];
        this.overview = data[3];
        this.id = data[4];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.title,
                this.image,
                this.date,
                this.overview,
                this.id});
    }
}
