package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.model.Device;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.ControlMQTT;
import com.hust.agriculture.payload.response.ResponseBean;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface DeviceService {
    void createDevice(String deviceName, ResponseBean bean) throws Exception;
    void findAllByUserId(Long userId, ResponseBean bean) throws Exception;
    void findAllByFarmId(Long userId, Long farmId, ResponseBean bean) throws Exception;

    boolean isOwnerByUser(Long userId, Long deviceId);

    void updateInfo(Device device, ResponseBean bean) throws Exception;

    void createInfo(Device device, ResponseBean bean) throws Exception;

    void changeStatus(Long deviceId, Integer status, ResponseBean bean) throws Exception;

    void getDevices(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception;

    ByteArrayInputStream getData(Long deviceId) throws Exception;

    void control(ResponseBean bean, User userActive, ControlMQTT controlMQTT) throws Exception;
}
