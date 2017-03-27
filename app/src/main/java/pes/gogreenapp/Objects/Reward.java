package pes.gogreenapp.Objects;

import java.util.Date;

/**
 * Created by Albert on 19/03/2017.
 */

public class Reward {

    private Integer id;
    private String title;
    private Integer points;
    private Date endDate;
    private String category;

    public Reward(Integer id, String title, Integer points, Date date, String category) {
        this.id = id;
        this.title = title;
        this.points = points;
        this.endDate = date;
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setDate(Date date) {
        this.endDate = date;
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

    public Date getEndDate() {
        return endDate;
    }

    public String getCategory() {
        return category;
    }
}
