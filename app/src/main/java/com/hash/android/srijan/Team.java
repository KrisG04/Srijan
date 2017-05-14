package com.hash.android.srijan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Spandita Ghosh on 2/7/2017.
 */

public class Team implements Parcelable {
    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
    private String name;
    private String shortDescription;
    private int ImageView;
    private int icon;
    private String longDescription;

    public Team(String name, String shortDescription, int imageView, int icon, String longDescription) {

        this.name = name;
        this.shortDescription = shortDescription;
        ImageView = imageView;
        this.icon = icon;
        this.longDescription = longDescription;
    }

    protected Team(Parcel in) {
        name = in.readString();
        shortDescription = in.readString();
        ImageView = in.readInt();
        icon = in.readInt();
        longDescription = in.readString();
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getImageView() {
        return ImageView;
    }

    public void setImageView(int imageView) {
        ImageView = imageView;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(shortDescription);
        parcel.writeInt(ImageView);
        parcel.writeInt(icon);
        parcel.writeString(longDescription);
    }
}
