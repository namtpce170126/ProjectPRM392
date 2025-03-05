package com.example.projectprm392.Models;

public class Account {
    private int accId;
    private int roleId;
    private String username;
    private String password;
    private String address;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String birthday;
    private int isDelete;

    public Account(int accId, int roleId, String username, String password, String address, String fullName, String phoneNumber, String email, String birthday, int isDelete) {
        this.accId = accId;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.address = address;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
        this.isDelete = isDelete;
    }
}
