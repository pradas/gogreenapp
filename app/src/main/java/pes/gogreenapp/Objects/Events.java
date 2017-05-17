package pes.gogreenapp.Objects;

/**
 * Created by Adrian on 17/05/2017.
 */

public class Events {
    private String title;
    private Integer points;

    public Events(String title, Integer points) {
        this.title = title;
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
