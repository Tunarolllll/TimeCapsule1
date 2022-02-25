package com.example.timecapsule.http;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HttpResponse<T> implements Serializable {
    private int errorCode;
    @SerializedName("reason")
    private String message;
    @SerializedName("result")
    private T results;


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", results=" + results +
                '}';
    }
}
