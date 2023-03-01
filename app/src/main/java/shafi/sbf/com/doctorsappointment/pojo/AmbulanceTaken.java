package shafi.sbf.com.doctorsappointment.pojo;

public class AmbulanceTaken {

    private String id;
    private String from;
    private String to;
    private String price;
    private String ambulanceType;
    private PatientDetails patientDetails;
    private String status;
    private String date;

    public AmbulanceTaken() {
    }


    public AmbulanceTaken(String id, String from, String to, String price, String ambulanceType, PatientDetails patientDetails, String status, String date) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.price = price;
        this.ambulanceType = ambulanceType;
        this.patientDetails = patientDetails;
        this.status = status;
        this.date = date;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmbulanceType() {
        return ambulanceType;
    }

    public void setAmbulanceType(String ambulanceType) {
        this.ambulanceType = ambulanceType;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
