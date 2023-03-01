package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class Appointments implements Serializable {

    private String appointmentKey;
    private String doctorID;
    private String chamberKey;
    private String patientID;
    private String patientFirstName;
    private String patientLastName;
    private String description;
    private String appointmentDate;
    private String patientImageURI;
    private String chamberArea;
    private String chamberCity;
    private String doctorName;
    private String doctorImage;
    private String doctorDegree;
    private String doctorSpeciality;
    private String doctorTime;

	public Appointments() {
    }

    public Appointments(String appointmentKey, String doctorID, String chamberKey, String patientID, String patientFirstName, String patientLastName, String description, String appointmentDate, String patientImageURI, String chamberArea, String chamberCity, String doctorName, String doctorImage, String doctorDegree, String doctorSpeciality, String doctorTime) {
        this.appointmentKey = appointmentKey;
        this.doctorID = doctorID;
        this.chamberKey = chamberKey;
        this.patientID = patientID;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.description = description;
        this.appointmentDate = appointmentDate;
        this.patientImageURI = patientImageURI;
        this.chamberArea = chamberArea;
        this.chamberCity = chamberCity;
        this.doctorName = doctorName;
        this.doctorImage = doctorImage;
        this.doctorDegree = doctorDegree;
        this.doctorSpeciality = doctorSpeciality;
        this.doctorTime = doctorTime;
    }

    public String getAppointmentKey() {
        return appointmentKey;
    }

    public void setAppointmentKey(String appointmentKey) {
        this.appointmentKey = appointmentKey;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getChamberKey() {
        return chamberKey;
    }

    public void setChamberKey(String chamberKey) {
        this.chamberKey = chamberKey;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPatientImageURI() {
        return patientImageURI;
    }

    public void setPatientImageURI(String patientImageURI) {
        this.patientImageURI = patientImageURI;
    }

    public String getChamberArea() {
        return chamberArea;
    }

    public void setChamberArea(String chamberArea) {
        this.chamberArea = chamberArea;
    }

    public String getChamberCity() {
        return chamberCity;
    }

    public void setChamberCity(String chamberCity) {
        this.chamberCity = chamberCity;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(String doctorImage) {
        this.doctorImage = doctorImage;
    }

    public String getDoctorDegree() {
        return doctorDegree;
    }

    public void setDoctorDegree(String doctorDegree) {
        this.doctorDegree = doctorDegree;
    }

    public String getDoctorSpeciality() {
        return doctorSpeciality;
    }

    public void setDoctorSpeciality(String doctorSpeciality) {
        this.doctorSpeciality = doctorSpeciality;
    }

    public String getDoctorTime() {
        return doctorTime;
    }

    public void setDoctorTime(String doctorTime) {
        this.doctorTime = doctorTime;
    }
}
