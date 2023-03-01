package shafi.sbf.com.doctorsappointment.pojo;

import java.util.List;

public class Chamber {

    private String add_ch_id;

    private String chamberName;

    private List<String> dateList;

    private String start;
    private String end;

    private String patient_count;

    private String house;
    private String floor;
    private String room;
    private String block;
    private String road;
    private String area;
    private String city;
    private String zip;
    private String country_id;
    private String fees;
    private String status;

    private String doctorID;

    public Chamber() {
    }

    public Chamber(String add_ch_id, String chamberName, List<String> dateList, String start, String end, String patient_count, String house, String floor, String room, String block, String road, String area, String city, String zip, String country_id, String fees, String status, String doctorID) {
        this.add_ch_id = add_ch_id;
        this.chamberName = chamberName;
        this.dateList = dateList;
        this.start = start;
        this.end = end;
        this.patient_count = patient_count;
        this.house = house;
        this.floor = floor;
        this.room = room;
        this.block = block;
        this.road = road;
        this.area = area;
        this.city = city;
        this.zip = zip;
        this.country_id = country_id;
        this.fees = fees;
        this.status = status;
        this.doctorID = doctorID;
    }

    public Chamber(String chamberName, List<String> dateList, String start, String end, String patient_count, String house, String floor, String room, String block, String road, String area, String city, String zip, String country_id, String fees, String status, String doctorID) {
        this.chamberName = chamberName;
        this.dateList = dateList;
        this.start = start;
        this.end = end;
        this.patient_count = patient_count;
        this.house = house;
        this.floor = floor;
        this.room = room;
        this.block = block;
        this.road = road;
        this.area = area;
        this.city = city;
        this.zip = zip;
        this.country_id = country_id;
        this.fees = fees;
        this.status = status;
        this.doctorID = doctorID;
    }

    public String getAdd_ch_id() {
        return add_ch_id;
    }

    public void setAdd_ch_id(String add_ch_id) {
        this.add_ch_id = add_ch_id;
    }

    public String getChamberName() {
        return chamberName;
    }

    public void setChamberName(String chamberName) {
        this.chamberName = chamberName;
    }

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPatient_count() {
        return patient_count;
    }

    public void setPatient_count(String patient_count) {
        this.patient_count = patient_count;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
}
