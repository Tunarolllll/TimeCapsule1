package com.example.timecapsule.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class CapsuleBean extends LitePalSupport implements Serializable {
    private long id;
    private String title;
    private String content;
    private String path;
    private String account;
    private long openTime;

    public CapsuleBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    @Override
    public String toString() {
        return "CapsuleBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", path='" + path + '\'' +
                ", openTime=" + openTime +
                '}';
    }
}
