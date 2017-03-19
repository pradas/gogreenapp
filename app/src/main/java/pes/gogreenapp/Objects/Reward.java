package pes.gogreenapp.Objects;

/**
 * Created by Albert on 19/03/2017.
 */

public class Reward {

    private Integer id;
    private String title;
    private Integer points;
    private String date;
    private String category;

    public Reward(Integer id, String title, Integer points, String date, String category) {
        this.id = id;
        this.title = title;
        this.points = points;
        this.date = date;
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoints(Integer points) {
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

    public Integer getPoints() {
        return points;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}
