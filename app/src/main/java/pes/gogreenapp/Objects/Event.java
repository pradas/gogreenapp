package pes.gogreenapp.Objects;

import android.util.Base64;

import java.util.Date;


public class Event {
    private Integer id;
    private String title;
    private String description;
    private Integer points;
    private String direction;
    private String company;
    private Date date;
    private String hour;
    private String min;
    private String category;
    private byte[] image;

    public Event(Integer id, String title, String description, Integer points, String direction,String company, Date date, String image, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.points = points;
        this.direction = direction;
        this.company = company;
        this.date = date;
        this.hour = String.valueOf(date.getHours());
        this.min = String.valueOf(date.getMinutes());
        this.category = category;
        if (image != null) {
            this.image = Base64.decode(image, 0);
        }

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHour() { return this.hour; }

    public String getMin() { return this.min; }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

