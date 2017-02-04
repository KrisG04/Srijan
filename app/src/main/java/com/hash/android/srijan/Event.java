package com.hash.android.srijan;

/**
 * Created by Spandita Ghosh on 2/2/2017.
 */
public class Event {
    private String category;
    private String head;
    private String content;
    private int image;
    private int icon;
    private String detailedDescription;

    public Event(String category, String head, String content, int image, int icon, String detailedDescription) {
        this.category = category;
        this.head = head;
        this.content = content;
        this.image = image;
        this.icon = icon;
        this.detailedDescription = detailedDescription;
    }

    public Event() {
        //Default constructor
    }

    public String getDetailedDescription() {

        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
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

    public void save() {
    }
}
