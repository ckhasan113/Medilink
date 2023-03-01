package shafi.sbf.com.doctorsappointment.pojo;

public class DoctorDates {

    private String chamberID;
    private String date;

    public DoctorDates() {
    }

    public DoctorDates(String chamberID, String date) {
        this.chamberID = chamberID;
        this.date = date;
    }

    public String getChamberID() {
        return chamberID;
    }

    public void setChamberID(String chamberID) {
        this.chamberID = chamberID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
