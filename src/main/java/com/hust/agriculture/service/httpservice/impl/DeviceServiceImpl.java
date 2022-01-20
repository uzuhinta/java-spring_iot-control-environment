package com.hust.agriculture.service.httpservice.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Device;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.ControlMQTT;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.repository.DeviceRepository;
import com.hust.agriculture.service.httpservice.DeviceService;
import com.hust.agriculture.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public void createDevice(String deviceName, ResponseBean bean) throws Exception {
        // check if device name is exists
        Device device = deviceRepository.findByDeviceName(deviceName);
        if(device != null){
            bean.setMessage(Constant.MSG_DEVICE_NAME_IS_EXISTS);
            return;
        }

        // generate topic name
        String topicName;
        do {
            UUID uuid = UUID.randomUUID();
            topicName = Constant.CONTROL_TOPIC + uuid.toString();
            device = deviceRepository.findByTopicName(topicName);
        } while (device != null);

        // save device
        device = new Device();
        device.setDeviceName(deviceName);
        device.setStatus(Constant.STATUS_ACTIVE);
        device.setTopicName(topicName);
        device.setFarm(null);
        deviceRepository.save(device);

        // response
        bean.addData("topicName", device.getTopicName());
        bean.addData("deviceId", device.getID());
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void findAllByUserId(Long userId, ResponseBean bean) throws Exception {
        List<Device> devices = deviceRepository.findByUserId(userId);
        bean.addData("devices", devices);
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void findAllByFarmId(Long userId, Long farmId, ResponseBean bean) throws Exception {
        List<Device> devices = deviceRepository.findByFarmId(userId, farmId);
        bean.addData("devices", devices);
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public boolean isOwnerByUser(Long userId, Long deviceId) {
        Device device = deviceRepository.findByUserIdAndDeviceId(deviceId, userId);
        if(device != null){
            return true;
        }
        return false;
    }

    @Override
    public void updateInfo(Device device, ResponseBean bean) throws Exception {
        deviceRepository.saveAndFlush(device);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
    }

    @Override
    public void createInfo(Device device, ResponseBean bean) throws Exception {
        // check if device name is exists
        Device deviceOld = deviceRepository.findByDeviceName(device.getDeviceName());
        if(deviceOld != null){
            bean.setMessage(Constant.MSG_DEVICE_NAME_IS_EXISTS);
            return;
        }

        // generate topic name
        String topicName;
        do {
            UUID uuid = UUID.randomUUID();
            topicName = Constant.CONTROL_TOPIC + uuid.toString();
            deviceOld = deviceRepository.findByTopicName(topicName);
        } while (deviceOld != null);

        device.setStatus(Constant.STATUS_ACTIVE);
        device.setTopicName(topicName);
        deviceRepository.saveAndFlush(device);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_CREATE_INFO_SUCCESS);
    }

    @Override
    public void changeStatus(Long deviceId, Integer status, ResponseBean bean) throws Exception {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if(device != null){
            device.setStatus(status);
            deviceRepository.saveAndFlush(device);
            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
        }else{
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
        }
    }

    @Override
    public void getDevices(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Device> pages;
        if(CommonUtils.isSearchExactly(key)){
            key = key.substring(1, key.length()-1);
            pages = deviceRepository.findDevicesExactly(pageable, status, key);
        }else{
            pages = deviceRepository.findDevices(pageable, status, key);
        }
        bean.addData("total", pages.getTotalElements());
        bean.addData("items", pages.getContent());
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public ByteArrayInputStream getData(Long deviceId) throws Exception {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if(device != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StringBuilder properties = new StringBuilder();
            properties.append("token=").append(device.getID()+"\n");
            properties.append("commonTopic=").append(Constant.COMMON_TOPIC+"\n");
            properties.append("controlTopic=").append(device.getTopicName()+"\n");
            properties.append("isStart=True\n");
            properties.append("brokerServer=tcp://broker.hivemq.com:1883");
            outputStream.write(properties.toString().getBytes(StandardCharsets.UTF_8));
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
        return null;
    }
}
