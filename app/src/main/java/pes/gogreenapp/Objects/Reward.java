package pes.gogreenapp.Objects;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Albert on 19/03/2017.
 */

public class Reward {
    private Integer id;
    private String title;
    private Integer points;
    private Date endDate;
    //private Date exchangeDate;
    private String category;
    private String description;
    private String info;
    private String contactWeb;
    private String contactInfo;
    private Double exchangeLatitude;
    private Double exchangeLenght;
    private boolean favorite;
    private Bitmap image;

    /**
     * Constructor of the Reward Object.
     *
     * @param id       of the Reward.
     * @param title    of the Reward.
     * @param points   of the Reward.
     * @param endDate  of the Reward.
     * @param category of the Reward.
     * @param favorite of the Reward.
     * @param image of the Reward.
     */
    public Reward(Integer id, String title, Integer points, Date endDate, String category, Boolean favorite, Bitmap image) {
        this.id = id;
        this.title = title;
        this.points = points;
        this.endDate = endDate;
        this.category = category;
        this.favorite = favorite;
        this.image = image;
    }

    /**
     * Constructor of the Reward Object.
     *
     * @param id       of the Reward.
     * @param title    of the Reward.
     * @param points   of the Reward.
     * @param endDate  of the Reward.
     * @param description of the Reward.
     * @param info of the Reward.
     * @param contactWeb of the Reward.
     * @param contactInfo       of the Reward.
     * @param exchangeLatitude    of the Reward.
     * @param exchangeLenght   of the Reward.
     * @param favorite  of the Reward.
     */
    public Reward(Integer id, String title, Integer points, Date endDate, String description,
                  String info, String contactWeb, String contactInfo, Double exchangeLatitude,
                  Double exchangeLenght, Boolean favorite) {
        this.id = id;
        this.title = title;
        this.points = points;
        this.endDate = endDate;
        this.description = description;
        this.info = info;
        this.contactWeb = contactWeb;
        this.contactInfo = contactInfo;
        this.exchangeLatitude = exchangeLatitude;
        this.exchangeLenght = exchangeLenght;
        this.favorite = favorite;
    }

    /**
     * Setter of the Reward title.
     *
     * @param title new title of the Reward.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter of the Reward points.
     *
     * @param points new points of the Reward.
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     * Setter of the Reward end Date.
     *
     * @param endDate new end Date of the Reward.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Setter of the Reward category.
     *
     * @param category new category of the Reward.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Getter of the Reward id
     *
     * @return the Reward id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter of the Reward title
     *
     * @return the Reward title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter of the Reward points
     *
     * @return the Reward points
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * Getter of the Reward end Date
     *
     * @return the Reward end Date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Getter of the Reward category
     *
     * @return the Reward category
     */
    public String getCategory() {
        return category;
    }

    /*public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }*/

    /**
     * Getter of the Reward description
     *
     * @return the Reward description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter of the Reward description
     *
     * @param description new description of the Reward.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter of the Reward info
     *
     * @return the Reward info
     */
    public String getInfo() {
        return info;
    }

    /**
     * Setter of the Reward info
     *
     * @param info new info of the Reward.
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Getter of the Reward contact-web
     *
     * @return the Reward contact-web
     */
    public String getContactWeb() {
        return contactWeb;
    }

    /**
     * Setter of the Reward contactWeb
     *
     * @param contactWeb new contactWeb of the Reward.
     */
    public void setContactWeb(String contactWeb) {
        this.contactWeb = contactWeb;
    }

    /**
     * Getter of the Reward contact info
     *
     * @return the Reward contact info
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Setter of the Reward contactInfo
     *
     * @param contactInfo new contactInfo of the Reward.
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Getter of the Reward exchange latitude
     *
     * @return the Reward eschange latitude
     */
    public Double getExchangeLatitude() {
        return exchangeLatitude;
    }

    /**
     * Setter of the Reward exchangeLatitude
     *
     * @param exchangeLatitude new exchangeLatitude of the Reward.
     */
    public void setExchangeLatitude(Double exchangeLatitude) {
        this.exchangeLatitude = exchangeLatitude;
    }

    /**
     * Getter of the Reward exchange lenght
     *
     * @return the Reward exchange lenght
     */
    public Double getExchangeLenght() {
        return exchangeLenght;
    }

    /**
     * Setter of the Reward exchangeLenght
     *
     * @param exchangeLenght new exchangeLenght of the Reward.
     */
    public void setExchangeLenght(Double exchangeLenght) {
        this.exchangeLenght = exchangeLenght;
    }

    /**
     * Getter of the Reward favorite
     *
     * @return if the reward is favorite
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Setter of the Reward favorite
     *
     * @param favorite new state of the reward
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    /**
     * Getter of the Reward image
     *
     * @return the Reward image
     */
    public Bitmap getImage() {
        return image;
    }


    /**
     * Setter of the Reward image
     *
     * @param image new category of the Reward.
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

}

