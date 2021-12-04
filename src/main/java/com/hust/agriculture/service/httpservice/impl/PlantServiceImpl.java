package com.hust.agriculture.service.httpservice.impl;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.Plant;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.repository.PlantRepository;
import com.hust.agriculture.service.httpservice.PlantService;
import com.hust.agriculture.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlantServiceImpl implements PlantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantServiceImpl.class);

    @Autowired
    PlantRepository plantRepository;


    @Override
    public void getPlants(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Plant> pages = plantRepository.findPlants(pageable, status, key);
        bean.addData("total", pages.getTotalElements());
        bean.addData("items", pages.getContent());
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void updateInfo(Plant plant, ResponseBean bean) throws Exception {
        plantRepository.saveAndFlush(plant);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
    }

    @Override
    public void createInfo(Plant plant, ResponseBean bean) throws Exception {
        plant.setStatus(Constant.STATUS_ACTIVE);
        plantRepository.saveAndFlush(plant);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_CREATE_INFO_SUCCESS);
    }

    @Override
    public void changeStatus(Long plantId, Integer status, ResponseBean bean) throws Exception {
        Plant plant = plantRepository.findById(plantId).orElse(null);
        if(plant != null){
            plant.setStatus(status);
            plantRepository.saveAndFlush(plant);
            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
        }else{
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
        }
    }
}
