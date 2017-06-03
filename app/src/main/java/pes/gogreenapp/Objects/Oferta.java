package pes.gogreenapp.Objects;

import android.util.Base64;

import java.util.Date;


public class Oferta {
    private Integer id;
    private String title;
    private String description;
    private Integer points;
    private Date date;
    private Boolean favorite;
    private byte[] image;
    private String shop;

    public Oferta(Integer id, String title, String description, Integer points, Date date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.points = points;
        this.date = date;

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
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public boolean isFavorite() { return favorite; }

    public void setFavorite( boolean favorite) { this.favorite = favorite; }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }
}

