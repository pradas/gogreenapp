package pes.gogreenapp.Objects;

/**
 * Created by Adrian on 02/06/2017.
 */

public class Shop {

    private String shopUrlImage;
    private String shopName;
    private String shopEmail;
    private String shopAddress;

    /**
     * Constructor of the Shop Object.
     *
     * @param shopUrlImage       of the Shop.
     * @param shopName    of the Shop.
     * @param shopEmail   of the Shop.
     * @param shopAddress  of the Shop.
     */
    public Shop(String shopUrlImage, String shopName, String shopEmail, String shopAddress) {
        this.shopUrlImage = shopUrlImage;
        this.shopName = shopName;
        this.shopEmail = shopEmail;
        this.shopAddress = shopAddress;
    }

    /**
     * Getter of the Sop urlImage
     *
     * @return the Shop urlImage
     */
    public String getShopUrlImage() {
        return shopUrlImage;
    }

    /**
     * Setter of the Shop urlImage.
     *
     * @param shopUrlImage new urlImage of the Shop.
     */
    public void setShopUrlImage(String shopUrlImage) {
        this.shopUrlImage = shopUrlImage;
    }

    /**
     * Getter of the Shop shopName
     *
     * @return the Shop shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * Setter of the Shop name.
     *
     * @param shopName new name of the Shop.
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * Getter of the Shop email
     *
     * @return the Shop email
     */
    public String getShopEmail() {
        return shopEmail;
    }

    /**
     * Setter of the Shop email.
     *
     * @param shopEmail new email of the Shop.
     */
    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    /**
     * Getter of the Shop address
     *
     * @return the Shop address
     */
    public String getShopAddress() {
        return shopAddress;
    }

    /**
     * Setter of the Shop address.
     *
     * @param shopAddress new address of the Shop.
     */
    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
}
