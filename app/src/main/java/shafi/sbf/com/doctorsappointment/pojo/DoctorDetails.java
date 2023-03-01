package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class DoctorDetails implements Serializable {

    private String doctorID;

    private String firstName;
    private String lastName;
    private String degree;
    private String department;
    private String registerNumber;
    private String university;
    private String experience;
    private String phone;
    private String email;
    private String password;
    private String imageURI;

    public DoctorDetails() {
    }

    public DoctorDetails(String doctorID, String firstName, String lastName, String degree, String department, String registerNumber, String university, String experience, String phone, String email, String password, String imageURI) {
        this.doctorID = doctorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.degree = degree;
        this.department = department;
        this.registerNumber = registerNumber;
        this.university = university;
        this.experience = experience;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.imageURI = imageURI;
    }


    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
