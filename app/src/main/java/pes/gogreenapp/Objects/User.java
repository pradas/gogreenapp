package pes.gogreenapp.Objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Dani
 */

public class User {

    private String username;
    private String name;
    private String email;
    private Integer currentPoints;
    private Integer totalPoints;
    private Date birthDate;
    private Date creationDate;
    private String userUrlImage;

    // fields of User table on gogreen.db
    private String role;
    private String token;

    //Calendar cal = GregorianCalendar.getInstance();
    private DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

    public User(String username, String name, String email, String birthDate, String userUrlImage,
                Integer totalPoints, Integer currentPoints, String creationDate) {

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
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public Integer getCurrentPoints() {

        return currentPoints;
    }

    public void setCurrentPoints(Integer currentPoints) {

        this.currentPoints = currentPoints;
    }

    public Integer getTotalPoints() {

        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {

        this.totalPoints = totalPoints;
    }

    public Date getBirthDate() {

        return birthDate;
    }

    public void setBirthDate(Date birthDate) {

        this.birthDate = birthDate;
    }

    public Date getCreationDate() {

        return creationDate;
    }

    public void setCreationDate(Date creationDate) {

        this.creationDate = creationDate;
    }

    public String getUserUrlImage() {

        return userUrlImage;
    }

    public void setUserUrlImage(String userUrlImage) {

        this.userUrlImage = userUrlImage;
    }

    public String getRole() {

        return role;
    }

    public String getToken() {

        return token;
    }
}
