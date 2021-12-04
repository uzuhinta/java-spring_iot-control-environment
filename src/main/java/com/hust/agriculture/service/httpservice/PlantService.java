package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.Plant;
import com.hust.agriculture.payload.response.ResponseBean;

public interface PlantService {

    void getPlants(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception;

    void updateInfo(Plant plant, ResponseBean bean) throws Exception;

    void createInfo(Plant plant, ResponseBean bean) throws Exception;

    void changeStatus(Long plantId, Integer status, ResponseBean bean) throws Exception;
}
