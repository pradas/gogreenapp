package pes.gogreenapp.Objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Dani
 */

public class User {

    private Integer shopId;
    private String username;
    private String name;
    private String email;
    private Integer currentPoints;
    private Integer totalPoints;
    private Date birthDate;
    private Date creationDate;
    private String userUrlImage;

    /**
     * fields of User table on gogreen.db
      */
    private String role;
    private String token;

    /**
     * Calendar cal = GregorianCalendar.getInstance();
     */
    private DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * Constructor of the Shop Object.
     *
     * @param username       of the User
     * @param name    of the User.
     * @param email   of the User.
     * @param birthDate  of the User.
     * @param userUrlImage       of the User.
     * @param totalPoints    of the User.
     * @param currentPoints   of the User.
     * @param creationDate  of the User.
     */
    public User(String username, String name, String email, String birthDate, String userUrlImage, Integer totalPoints,
                Integer currentPoints, String creationDate) {

        this.username = username;
        this.name = name;
        this.email = email;
        try {
            this.birthDate = sourceFormat.parse((String) birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.currentPoints = currentPoints;
        this.totalPoints = totalPoints;
        try {
            this.creationDate = sourceFormat.parse((String) creationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.userUrlImage = userUrlImage;
    }

    /**
     * Constructor of the Shop Object.
     *
     * @param username       of the User
     * @param name    of the User.
     * @param email   of the User.
     * @param birthDate  of the User.
     * @param userUrlImage       of the User.
     * @param totalPoints    of the User.
     * @param currentPoints   of the User.
     */
    public User(String username, String name, String email, String birthDate, String userUrlImage, int totalPoints,
                int currentPoints) {

        this.username = username;
        this.name = name;
        this.email = email;
        try {
            this.birthDate = sourceFormat.parse((String) birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.currentPoints = currentPoints;
        this.totalPoints = totalPoints;
        this.creationDate = new Date();
        this.userUrlImage = userUrlImage;
    }

    /**
     * Constructor for the User returned by the SQLite DB
     *
     * @param username username of the new User
     * @param token    API token of the new User
     * @param role     app role of the new User
     * @param points   current points of the new User
     */
    public User(String username, String token, String role, Integer points) {

        this.username = username;
        this.token = token;
        this.role = role;
        this.currentPoints = points;
        this.shopId = shopId;
    }

    /**
     * Getter of the User username
     *
     * @return the User username
     */
    public String getUsername() {

        return username;
    }

    /**
     * Setter of the User username.
     *
     * @param username new username of the User.
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Getter of the User name
     *
     * @return the User name
     */
    public String getName() {

        return name;
    }

    /**
     * Setter of the User name.
     *
     * @param name new username of the User.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Getter of the User email
     *
     * @return the User email
     */
    public String getEmail() {

        return email;
    }

    /**
     * Setter of the User email.
     *
     * @param email new email of the User.
     */
    public void setEmail(String email) {

        this.email = email;
    }

    /**
     * Getter of the User current points
     *
     * @return the User current points
     */
    public Integer getCurrentPoints() {

        return currentPoints;
    }

    /**
     * Setter of the User current points.
     *
     * @param currentPoints new current points of the User.
     */
    public void setCurrentPoints(Integer currentPoints) {

        this.currentPoints = currentPoints;
    }

    /**
     * Getter of the User total points
     *
     * @return the User total points
     */
    public Integer getTotalPoints() {

        return totalPoints;
    }

    /**
     * Setter of the User total points.
     *
     * @param totalPoints new total points of the User.
     */
    public void setTotalPoints(Integer totalPoints) {

        this.totalPoints = totalPoints;
    }

    /**
     * Getter of the User birth date
     *
     * @return the User birth date
     */
    public Date getBirthDate() {

        return birthDate;
    }

    /**
     * Setter of the User birth date.
     *
     * @param birthDate new birthDate of the User.
     */
    public void setBirthDate(Date birthDate) {

        this.birthDate = birthDate;
    }

    /**
     * Getter of the User creation date
     *
     * @return the User creation date
     */
    public Date getCreationDate() {

        return creationDate;
    }

    /**
     * Setter of the User creationDate.
     *
     * @param creationDate new creationDate of the User.
     */
    public void setCreationDate(Date creationDate) {

        this.creationDate = creationDate;
    }

    /**
     * Getter of the User url image
     *
     * @return the User url image
     */
    public String getUserUrlImage() {

        return userUrlImage;
    }

    /**
     * Setter of the User url image.
     *
     * @param userUrlImage new urlImage of the User.
     */
    public void setUserUrlImage(String userUrlImage) {

        this.userUrlImage = userUrlImage;
    }

    /**
     * Getter of the User role
     *
     * @return the User role
     */
    public String getRole() {

        return role;
    }

    /**
     * Getter of the User token
     *
     * @return the User token
     */
    public String getToken() {

        return token;
    }

    /**
     * Getter of the User shop id
     *
     * @return the User shop id
     */
    public Integer getShopId() {

        return shopId;
    }

    /**
     * Setter of the User shop id.
     *
     * @param shopId new shopId of the User.
     */
    public void setShopId(Integer shopId) {

        this.shopId = shopId;
    }
}
