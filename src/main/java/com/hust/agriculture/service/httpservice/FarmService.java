package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.model.Farm;
import com.hust.agriculture.payload.response.ResponseBean;

public interface FarmService {

    void findAllByUserId(Long userId, ResponseBean bean) throws Exception;

    void getFarms(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception;

    void getFarmsByName(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception;

    void updateInfo(Farm farm, ResponseBean bean) throws Exception;

    void createInfo(Farm farm, ResponseBean bean) throws Exception;

    void changeStatus(Long farmId, Integer status, ResponseBean bean) throws Exception;
}
