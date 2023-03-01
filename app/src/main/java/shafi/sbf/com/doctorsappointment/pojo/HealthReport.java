package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class HealthReport implements Serializable {
    private String id;
    private String image;
    private String fileName;
    private String doctorName;
    private String date;

    public HealthReport() {
    }

    public HealthReport(String id, String image, String fileName, String doctorName, String date) {
        this.id = id;
        this.image = image;
        this.fileName = fileName;
        this.doctorName = doctorName;
        this.date = date;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
