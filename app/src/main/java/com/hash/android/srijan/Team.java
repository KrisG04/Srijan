package com.hash.android.srijan;

/**
 * Created by Spandita Ghosh on 2/7/2017.
 */

public class Team {
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
}
