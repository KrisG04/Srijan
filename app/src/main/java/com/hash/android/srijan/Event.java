package com.hash.android.srijan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Spandita Ghosh on 2/2/2017.
 */
public class Event implements Parcelable {
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    private String category;
    private String head;
    private String content;
    private int image;
    private int icon;
    private String detailedDescription;
    private boolean isInterested;

    public Event(String category, String head, String content, int image, int icon, String detailedDescription) {
        this.category = category;
        this.head = head;
        this.content = content;
        this.image = image;
        this.icon = icon;
        this.detailedDescription = detailedDescription;
    }

    public Event(String category, String head, String content, int image, int icon, String detailedDescription, boolean isInterested) {
        this.category = category;
        this.head = head;
        this.content = content;
        this.image = image;
        this.icon = icon;
        this.detailedDescription = detailedDescription;
        this.isInterested = isInterested;
    }

    public Event() {
        //Default constructor
    }

    protected Event(Parcel in) {
        category = in.readString();
        head = in.readString();
        content = in.readString();
        image = in.readInt();
        icon = in.readInt();
        detailedDescription = in.readString();
        isInterested = in.readByte() != 0;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public boolean isInterested() {
        return isInterested;
    }

    public void setInterested(boolean interested) {
        isInterested = interested;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(head);
        parcel.writeString(content);
        parcel.writeInt(image);
        parcel.writeInt(icon);
        parcel.writeString(detailedDescription);
        parcel.writeByte((byte) (isInterested ? 1 : 0));
    }
}
