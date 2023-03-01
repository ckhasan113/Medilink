package shafi.sbf.com.doctorsappointment.pojo;

import java.io.Serializable;

public class PackageDetails implements Serializable {

    String packageId;
    String packageName;
    String packagePrice;
    String Start;
    String End;

    public PackageDetails() {
    }

    public PackageDetails(String packageId, String packageName, String packagePrice, String start, String end) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        Start = start;
        End = end;
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

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }
}
