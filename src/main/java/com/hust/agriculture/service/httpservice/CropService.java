package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.payload.request.CropDTO;
import com.hust.agriculture.payload.response.ResponseBean;

public interface CropService {

    void createCrop(CropDTO cropDTO, ResponseBean bean) throws Exception;

    void getCrops(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception;

}
