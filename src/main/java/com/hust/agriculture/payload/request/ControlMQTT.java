package com.hust.agriculture.payload.request;

import java.io.Serializable;
import java.util.LinkedHashMap;
import javax.validation.constraints.NotNull;

public class ControlMQTT implements Serializable {

    @NotNull
    private Integer commandType;
    @NotNull
    private Long deviceId;
    private String description;
    @NotNull
    private LinkedHashMap<String, Object> data;

    public Integer getCommandType() {
        return commandType;
    }

    public void setCommandType(Integer commandType) {
        this.commandType = commandType;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedHashMap<String, Object> getData() {
        return data;
    }

    public void setData(LinkedHashMap<String, Object> data) {
        this.data = data;
    }
}
