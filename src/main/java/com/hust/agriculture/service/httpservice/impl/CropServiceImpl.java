package com.hust.agriculture.service.httpservice.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Crop;
import com.hust.agriculture.model.Device;
import com.hust.agriculture.model.Plant;
import com.hust.agriculture.payload.request.ControlMQTT;
import com.hust.agriculture.payload.request.CropDTO;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.repository.CropRepository;
import com.hust.agriculture.repository.DeviceRepository;
import com.hust.agriculture.repository.PlantRepository;
import com.hust.agriculture.service.httpservice.CropService;
import com.hust.agriculture.service.mqttservice.MqttControlService;
import com.hust.agriculture.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class CropServiceImpl implements CropService {

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    CropRepository cropRepository;

    @Autowired
    MqttControlService mqttControlService;

    @Override
    public void createCrop(CropDTO cropDTO, ResponseBean bean) throws Exception {
        if (isValid(cropDTO, bean)) {
            Plant plant = plantRepository.findById(cropDTO.getPlantId()).orElse(null);
            if (ObjectUtils.isEmpty(plant)) {
                bean.setMessage(Constant.MSG_PLANT_ID_INVALID);
                return;
            }

            List<Device> devices = new ArrayList<>();
            for (Long deviceId : cropDTO.getDeviceIds()) {
                Device device = deviceRepository.findById(deviceId).orElse(null);
                if (ObjectUtils.isEmpty(plant)) {
                    bean.setMessage(Constant.MSG_DEVICE_ID_INVALID);
                    return;
                } else {
                    devices.add(device);
                }
            }

            if (cropDTO.getPaymentType() != Constant.PAYMENT_BY_DAY && cropDTO.getPaymentType() != Constant.PAYMENT_BY_CROP) {
                bean.setMessage(Constant.MSG_PAYMENT_METHOD_INVALID);
                return;
            }

            if (cropDTO.getStartTime().before(new Date())) {
                bean.setMessage(Constant.MSG_START_DATE_INVALID);
                return;
            }

            if (cropDTO.getStartTime().after(cropDTO.getEndTime())) {
                bean.setMessage(Constant.MSG_END_DATE_INVALID);
                return;
            }

            Crop crop = new Crop();
            crop.setName(cropDTO.getCropName());
            crop.setPlant(plant);
            crop.setPaymentType(cropDTO.getPaymentType());
            crop.setStartTime(cropDTO.getStartTime());
            crop.setEndTime(cropDTO.getEndTime());
            crop.setStatus(Constant.STATUS_ACTIVE);
            cropRepository.save(crop);

            for(int i=0; i<devices.size(); i++){
                devices.get(i).setCrop(crop);
            }

            deviceRepository.saveAll(devices);
            startAllDevice(devices);

            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_CROP_CREATED);
        }
    }

    @Override
    public void getCrops(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Device> pages = deviceRepository.findDevicesCrop(pageable, status, key);
        bean.addData("total", pages.getTotalElements());
        bean.addData("items", pages.getContent());
        bean.setError(Constant.ERROR_CODE_OK);
    }

    private void startAllDevice(List<Device> devices) throws JsonProcessingException {

        ControlMQTT controlRequest = new ControlMQTT();
        controlRequest.setCommandType(Constant.START_COLLECT_DATA);
        controlRequest.setDescription(Constant.DES_COLLECT_DATE);

        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(controlRequest);

        for (Device device : devices) {
            mqttControlService.publish(device.getTopicName(), msg);
        }
    }

    private boolean isValid(CropDTO cropDTO, ResponseBean bean) {

        // check empty
        if (ObjectUtils.isEmpty(cropDTO.getCropName())) {
            bean.setMessage(Constant.MSG_CROP_NAME_IS_EMPTY);
            return false;
        }

        if (ObjectUtils.isEmpty(cropDTO.getPlantId())) {
            bean.setMessage(Constant.MSG_PAYMENT_TYPE_INVALID);
            return false;
        }

        if (ObjectUtils.isEmpty(cropDTO.getPaymentType())) {
            bean.setMessage(Constant.MSG_PAYMENT_TYPE_INVALID);
            return false;
        }

        if (ObjectUtils.isEmpty(cropDTO.getDeviceIds())) {
            bean.setMessage(Constant.MSG_DEVICE_ID_INVALID);
            return false;
        }

        if (ObjectUtils.isEmpty(cropDTO.getStartTime())) {
            bean.setMessage(Constant.MSG_START_DATE_INVALID);
            return false;
        }

        if (ObjectUtils.isEmpty(cropDTO.getEndTime())) {
            bean.setMessage(Constant.MSG_END_DATE_INVALID);
            return false;
        }


        return true;
    }
}
