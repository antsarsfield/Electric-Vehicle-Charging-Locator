package com.fyp.electricvehicle;
public class charginpoitnsdataclass {
    String ChargeDeviceName,ChargeDeviceRef,Latitude,Longitude,address,ConnectorType;
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

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getAddress() {
        return address;
    }


}
