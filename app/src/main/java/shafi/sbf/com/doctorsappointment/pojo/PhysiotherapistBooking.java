package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class PhysiotherapistBooking implements Serializable {

    private String physioBookingID;

    private PhysiotherapistVendorDetails vendorDetails;

    private PhysiotherapistPackageDetails packageDetails;

    private String prescriptionImage;

    private String refDoctorName;

    private String bookingDate;

    private PatientDetails patientDetails;

    public PhysiotherapistBooking() {
    }

    public PhysiotherapistBooking(String physioBookingID, PhysiotherapistVendorDetails vendorDetails, PhysiotherapistPackageDetails packageDetails, String prescriptionImage, String refDoctorName, String bookingDate, PatientDetails patientDetails) {
        this.physioBookingID = physioBookingID;
        this.vendorDetails = vendorDetails;
        this.packageDetails = packageDetails;
        this.prescriptionImage = prescriptionImage;
        this.refDoctorName = refDoctorName;
        this.bookingDate = bookingDate;
        this.patientDetails = patientDetails;
    }

    public String getPhysioBookingID() {
        return physioBookingID;
    }

    public void setPhysioBookingID(String physioBookingID) {
        this.physioBookingID = physioBookingID;
    }

    public PhysiotherapistVendorDetails getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(PhysiotherapistVendorDetails vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    public PhysiotherapistPackageDetails getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(PhysiotherapistPackageDetails packageDetails) {
        this.packageDetails = packageDetails;
    }

    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public String getRefDoctorName() {
        return refDoctorName;
    }

    public void setRefDoctorName(String refDoctorName) {
        this.refDoctorName = refDoctorName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }
}
