package pes.gogreenapp.Objects;

import java.util.Date;

/**
 * Created by Adrian on 17/04/2017.
 */

public class RewardDetailed {
    private Integer id;
    private String title;
    private Integer points;
    private Date endDate;
    private String description;
    private String info;
    private String contactWeb;
    private String contactInfo;
    private Integer exchangeLatitude;
    private Integer exchangeLenght;

    public RewardDetailed(Integer id, String title, Integer points, Date endDate,String description,
                          String info, String contactWeb, String contactInfo, Integer exchangeLatitude,
                          Integer exchangeLenght) {
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
    }

    public Integer getId() {
        return id;
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

    public String getDescription() {
        return description;
    }

    public String getInfo() {
        return info;
    }

    public String getContactWeb() {
        return contactWeb;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public Integer getExchangeLatitude() {
        return exchangeLatitude;
    }

    public Integer getExchangeLenght() {
        return exchangeLenght;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setContactWeb(String contactWeb) {
        this.contactWeb = contactWeb;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setExchangeLatitude(Integer exchangeLatitude) {
        this.exchangeLatitude = exchangeLatitude;
    }

    public void setExchangeLenght(Integer exchangeLenght) {
        this.exchangeLenght = exchangeLenght;
    }
}
