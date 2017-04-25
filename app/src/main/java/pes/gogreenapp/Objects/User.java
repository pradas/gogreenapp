package pes.gogreenapp.Objects;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.SECOND;

/**
 * Created by Dani on 13/04/2017.
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
    //Calendar cal = GregorianCalendar.getInstance();
    DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");

    public User(String username, String name, String email, String birthDate, String userUrlImage) {
        this.username = username;
        this.name = name;
        this.email = email;
        try {
            this.birthDate = sourceFormat.parse((String) birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.currentPoints = 0;
        this.totalPoints = 0;
        this.creationDate = new Date();
        this. userUrlImage = userUrlImage;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getName() { return name;  }

    public void setName(String name) { this.name = name;  }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Integer getCurrentPoints() { return currentPoints; }

    public void setCurrentPoints(Integer currentPoints) { this.currentPoints = currentPoints; }

    public Integer getTotalPoints() { return totalPoints;  }

    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints;  }

    public Date getBirthDate() { return birthDate; }

    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public Date getCreationDate() { return creationDate; }

    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public String getUserUrlImage() { return userUrlImage; }

    public void setUserUrlImage(String userUrlImage) { this.userUrlImage = userUrlImage; }
}
