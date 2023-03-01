package shafi.sbf.com.doctorsappointment.pojo;

public class ForeignBooking {

    private String bookingID;
    private String bookingDate;
    private String description;


    private ForeignDoctorDetails foreignDoctorDetails;
    private ForeignHospitalDetails foreignHospitalDetails;
    private PatientDetails patientDetails;


    public ForeignBooking() {
    }

    public ForeignBooking(String bookingID, String bookingDate, String description, ForeignDoctorDetails foreignDoctorDetails, ForeignHospitalDetails foreignHospitalDetails, PatientDetails patientDetails) {
        this.bookingID = bookingID;
        this.bookingDate = bookingDate;
        this.description = description;
        this.foreignDoctorDetails = foreignDoctorDetails;
        this.foreignHospitalDetails = foreignHospitalDetails;
        this.patientDetails = patientDetails;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ForeignDoctorDetails getForeignDoctorDetails() {
        return foreignDoctorDetails;
    }

    public void setForeignDoctorDetails(ForeignDoctorDetails foreignDoctorDetails) {
        this.foreignDoctorDetails = foreignDoctorDetails;
    }

    public ForeignHospitalDetails getForeignHospitalDetails() {
        return foreignHospitalDetails;
    }

    public void setForeignHospitalDetails(ForeignHospitalDetails foreignHospitalDetails) {
        this.foreignHospitalDetails = foreignHospitalDetails;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }
}
