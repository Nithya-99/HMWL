package com.example.hmwl;

public class AddressesModel {

    private String fullname;
//    private String mobileNo;
    private String address;
    private String pincode;

    public AddressesModel(String fullname, String address, String pincode) {
        this.fullname = fullname;
//        this.mobileNo = mobileNo;
        this.address = address;
        this.pincode = pincode;
    }

//    public String getMobileNo() {
//        return mobileNo;
//    }
//
//    public void setMobileNo(String mobileNo) {
//        this.mobileNo = mobileNo;
//    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

}

