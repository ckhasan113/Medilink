package shafi.sbf.com.doctorsappointment.pojo;

public class NurseAddtoCart {

    private String nurseTakenId;
    private String nurseTakenDate;
    private String nurseTakenPrectionImage;
    private String nurseTakenDoctorRef;
    private VendorDetails vendorDetails;
    private PackageDetails packageDetails;
    private PatientDetails patientDetails;
    private String status;

    public NurseAddtoCart() {

    }

    public NurseAddtoCart(String nurseTakenId, String nurseTakenDate, String nurseTakenPrectionImage, String nurseTakenDoctorRef, VendorDetails vendorDetails, PackageDetails packageDetails, PatientDetails patientDetails, String status) {
        this.nurseTakenId = nurseTakenId;
        this.nurseTakenDate = nurseTakenDate;
        this.nurseTakenPrectionImage = nurseTakenPrectionImage;
        this.nurseTakenDoctorRef = nurseTakenDoctorRef;
        this.vendorDetails = vendorDetails;
        this.packageDetails = packageDetails;
        this.patientDetails = patientDetails;
        this.status = status;
    }

    public String getNurseTakenId() {
        return nurseTakenId;
    }

    public void setNurseTakenId(String nurseTakenId) {
        this.nurseTakenId = nurseTakenId;
    }

    public String getNurseTakenDate() {
        return nurseTakenDate;
    }

    public void setNurseTakenDate(String nurseTakenDate) {
        this.nurseTakenDate = nurseTakenDate;
    }

    public String getNurseTakenPrectionImage() {
        return nurseTakenPrectionImage;
    }

    public void setNurseTakenPrectionImage(String nurseTakenPrectionImage) {
        this.nurseTakenPrectionImage = nurseTakenPrectionImage;
    }

    public String getNurseTakenDoctorRef() {
        return nurseTakenDoctorRef;
    }

    public void setNurseTakenDoctorRef(String nurseTakenDoctorRef) {
        this.nurseTakenDoctorRef = nurseTakenDoctorRef;
    }

    public VendorDetails getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(VendorDetails vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    public PackageDetails getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(PackageDetails packageDetails) {
        this.packageDetails = packageDetails;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
