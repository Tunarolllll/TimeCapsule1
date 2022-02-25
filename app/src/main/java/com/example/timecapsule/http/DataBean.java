package com.example.timecapsule.http;


import com.example.timecapsule.bean.NewsDetails;
import com.example.timecapsule.bean.NewsList;

import java.util.List;

public class DataBean {

    private String stat;
    private List<NewsList> data;
    private String content;
    private String uniquekey;
    private NewsDetails detail;
    public DataBean() {
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public NewsDetails getDetail() {
        return detail;
    }

    public void setDetail(NewsDetails detail) {
        this.detail = detail;
    }

    public List<NewsList> getData() {
        return data;
    }

    public void setData(List<NewsList> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "stat='" + stat + '\'' +
                ", data=" + data +
                ", content='" + content + '\'' +
                ", uniquekey='" + uniquekey + '\'' +
                ", detail=" + detail +
                '}';
    }
}
