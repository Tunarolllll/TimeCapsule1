package com.example.timecapsule.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class UserBean extends LitePalSupport implements Serializable {
    private long id;
    private String name;
    private String account;
    private String password;
    private int sex;
    private String image;

    public UserBean(String name, String account, String password) {
        this.name = name;
        this.account = account;
        this.password = password;
    }

    public UserBean() {
    }

    public long getId() {
        return id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
