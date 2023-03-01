package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class PhysiotherapistVendorDetails implements Serializable {

    private String vendorID;

    private String image;
    private String name;
    private String licence;
    private String area;
    private String city;
    private String history;
    private String phone;
    private String email;
    private String password;
    private String Status;

    public PhysiotherapistVendorDetails() {

    }

    public PhysiotherapistVendorDetails(String vendorID, String image, String name, String licence, String area, String city, String history, String phone, String email, String password, String status) {
        this.vendorID = vendorID;
        this.image = image;
        this.name = name;
        this.licence = licence;
        this.area = area;
        this.city = city;
        this.history = history;
        this.phone = phone;
        this.email = email;
        this.password = password;
        Status = status;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
