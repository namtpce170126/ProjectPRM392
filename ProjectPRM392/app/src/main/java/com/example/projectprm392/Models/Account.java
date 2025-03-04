package com.example.projectprm392.Models;

public class Account {
    private int accId;
    private int roleId;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String birthday;
    private int isDelete;

    public Account(int accId, int roleId, String password, String fullName, String phoneNumber, String email, String birthday, int isDelete) {
        this.accId = accId;
        this.roleId = roleId;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
        this.isDelete = isDelete;
    }
}
