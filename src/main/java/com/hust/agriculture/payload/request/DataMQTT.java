package com.hust.agriculture.payload.request;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class DataMQTT implements Serializable {

    private String token;
    private Integer type;
    private LinkedHashMap<String, Object> data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public LinkedHashMap<String, Object> getData() {
        return data;
    }

    public void setData(LinkedHashMap<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataMQTT{" +
                "token='" + token + '\'' +
                ", type=" + type +
                ", data=" + data +
                '}';
    }
}
