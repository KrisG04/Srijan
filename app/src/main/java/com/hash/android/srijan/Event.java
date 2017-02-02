package com.hash.android.srijan;

/**
 * Created by Spandita Ghosh on 2/2/2017.
 */
class Event {
    private String category;
    private String head;
    private String content;
    private int image;
    private int icon;

    public Event(String category, String head, String content, int image, int icon) {
        this.category = category;
        this.head = head;
        this.content = content;
        this.image = image;
        this.icon = icon;
    }

    public Event() {
        //Default constructor
    }

    public String getCategory() {
        return category;
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
