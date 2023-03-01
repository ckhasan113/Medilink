package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class HealthTips implements Serializable {
    private String id;
    private String image;
    private String tips;
    private String title;

    public HealthTips() {
    }

    public HealthTips(String id, String image, String tips, String title) {
        this.id = id;
        this.image = image;
        this.tips = tips;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
