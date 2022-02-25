package com.example.timecapsule.bean;

import org.litepal.crud.LitePalSupport;

public class CommentBean extends LitePalSupport {
    private long id;
    private String content;
    private long time;
    private long capseleId;
    private String account;

    public CommentBean(String content, long time, String account) {
        this.content = content;
        this.time = time;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public long getCapseleId() {
        return capseleId;
    }

    public void setCapseleId(long capseleId) {
        this.capseleId = capseleId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", account='" + account + '\'' +
                '}';
    }
}
