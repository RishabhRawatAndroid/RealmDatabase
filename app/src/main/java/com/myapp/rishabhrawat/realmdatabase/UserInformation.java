package com.myapp.rishabhrawat.realmdatabase;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rishabh Rawat on 7/25/2017.
 */

public class UserInformation extends RealmObject {

    private String name;
    @PrimaryKey  //define the phone number as a primary key
    private String phone;
    private String address;
    private String gmail;

    public UserInformation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}
