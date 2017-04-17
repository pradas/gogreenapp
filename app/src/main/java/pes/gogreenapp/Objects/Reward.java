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
    //private Date exchangeDate;
    private String category;

    /**
     * Constructor of the Reward Object.
     *
     * @param id       of the Reward.
     * @param title    of the Reward.
     * @param points   of the Reward.
     * @param endDate  of the Reward.
     * @param category of the Reward.
     */
    public Reward(Integer id, String title, Integer points, Date endDate, String category) {
        this.id = id;
        this.title = title;
        this.points = points;
        this.endDate = endDate;
        this.category = category;
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
}
