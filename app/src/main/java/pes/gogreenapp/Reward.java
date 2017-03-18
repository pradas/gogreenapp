package pes.gogreenapp;

/**
 * Created by Albert on 19/03/2017.
 */

class Reward {

    private String title;
    private String points;
    private String date;
    private String category;

    public Reward(String title, String points, String date, String category) {
        this.title = title;
        this.points = points;
        this.date = date;
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {

        return title;
    }

    public String getPoints() {
        return points;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}
