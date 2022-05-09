package com.fyp.electricvehicle;
public class charginpoitnsdataclass {
    String ChargeDeviceName,ChargeDeviceRef,Latitude,Longitude,address;
    public charginpoitnsdataclass(String chargeDeviceName, String chargeDeviceRef, String latitude, String longitude, String address) {
        ChargeDeviceName = chargeDeviceName;
        ChargeDeviceRef = chargeDeviceRef;
        Latitude = latitude;
        Longitude = longitude;
        this.address = address;
    }
    public String getChargeDeviceName() {
        return ChargeDeviceName;
    }
    public void setChargeDeviceName(String chargeDeviceName) {
        ChargeDeviceName = chargeDeviceName;
    }
    public String getChargeDeviceRef() {
        return ChargeDeviceRef;
    }
    public void setChargeDeviceRef(String chargeDeviceRef) {
        ChargeDeviceRef = chargeDeviceRef;
    }
    public String getLatitude() {
        return Latitude;
    }
    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
