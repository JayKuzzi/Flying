package com.bb.offerapp.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;


/**
 * Created by bb on 2017/5/15.
 */

public class User extends DataSupport implements Serializable {
    private String account;
    private String password;
    private String phone;
    private String email;
    private byte[] image;

    public User() {
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User(String account, String phone, String email, byte[] image) {
        this.account = account;
        this.phone = phone;
        this.email = email;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        User another = (User) obj;

        return account.equals(another.getAccount())&&password.equals(another.getPassword());
    }
}
