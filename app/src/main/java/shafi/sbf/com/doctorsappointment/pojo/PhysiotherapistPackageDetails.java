package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class PhysiotherapistPackageDetails implements Serializable {

    String packageId;
    String packageName;
    String packagePrice;
    String startTime;
    String endTime;

    public PhysiotherapistPackageDetails() {
    }

    public PhysiotherapistPackageDetails(String packageId, String packageName, String packagePrice, String startTime, String endTime) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
