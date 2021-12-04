package com.hust.agriculture.service.httpservice.impl;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.repository.FarmRepository;
import com.hust.agriculture.service.httpservice.FarmService;
import com.hust.agriculture.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmServiceImpl implements FarmService {

    @Autowired
    FarmRepository farmRepository;

    @Override
    public void findAllByUserId(Long userId, ResponseBean bean) throws Exception {
        List<Farm> farms = farmRepository.findByUserId(userId);
        bean.addData("farms", farms);
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void getFarms(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Farm> pages;
        if(CommonUtils.isSearchExactly(key)){
            key = key.substring(1, key.length()-1);
            pages = farmRepository.findFarmsExactly(pageable, status, key);
        }else{
            pages = farmRepository.findFarms(pageable, status, key);
        }
        bean.addData("total", pages.getTotalElements());
        bean.addData("items", pages.getContent());
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void getFarmsByName(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Farm> pages = farmRepository.findFarmsByName(pageable, status, key);
        bean.addData("total", pages.getTotalElements());
        bean.addData("items", pages.getContent());
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void updateInfo(Farm farm, ResponseBean bean) throws Exception {
        List<Farm> farms = farmRepository.findByName(farm.getName());
        if(farms.size() > 0){
            boolean isDuplicate = false;
            for(Farm f : farms){
                if(f.getID() != farm.getID()){
                    isDuplicate = true;
                }
            }
            if(isDuplicate) {
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setMessage(Constant.MSG_FARM_NAME_INVALID);
                return;
            }
        }
        farmRepository.saveAndFlush(farm);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
    }

    @Override
    public void createInfo(Farm farm, ResponseBean bean) throws Exception {
        List<Farm> farms = farmRepository.findByName(farm.getName());
        if(farms.size() > 0){
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_FARM_NAME_INVALID);
            return;
        }
        farm.setStatus(Constant.STATUS_ACTIVE);
        farmRepository.saveAndFlush(farm);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_CREATE_INFO_SUCCESS);
    }

    @Override
    public void changeStatus(Long farmId, Integer status, ResponseBean bean) throws Exception {
        Farm farm = farmRepository.findById(farmId).orElse(null);
        if(farm != null){
            farm.setStatus(status);
            farmRepository.saveAndFlush(farm);
            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
        }else{
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
        }
    }
}
