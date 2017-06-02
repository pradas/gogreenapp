package pes.gogreenapp.Objects;

/**
 * Created by Adrian on 02/06/2017.
 */

public class Shop {

    private String shopUrlImage;
    private String shopName;
    private String shopEmail;
    private String shopAddress;

    public Shop(String shopUrlImage, String shopName, String shopEmail, String shopAddress) {
        this.shopUrlImage = shopUrlImage;
        this.shopName = shopName;
        this.shopEmail = shopEmail;
        this.shopAddress = shopAddress;
    }

    public String getShopUrlImage() {
        return shopUrlImage;
    }

    public void setShopUrlImage(String shopUrlImage) {
        this.shopUrlImage = shopUrlImage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
}
