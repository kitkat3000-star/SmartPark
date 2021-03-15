package com.example.SmartParkVol2;

public class User {

    private String name, email, password,  phone_Num;
    private int license_Num;

    public User() {
    }

    public User(String name, String email, String password, int license_Num, String phone_Num) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.license_Num = license_Num;
        this.phone_Num = phone_Num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getLicense_Num() {
        return license_Num;
    }

    public void setLicense_Num(int license_Num) {
        this.license_Num = license_Num;
    }

    public String getPhone_Num() {
        return phone_Num;
    }

    public void setPhone_Num(String phone_Num) {
        this.phone_Num = phone_Num;
    }
}
