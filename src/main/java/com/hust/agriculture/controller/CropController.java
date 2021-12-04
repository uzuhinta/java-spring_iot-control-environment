package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.payload.request.CropDTO;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.service.httpservice.CropService;
import com.hust.agriculture.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secure")
public class CropController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CropController.class);

    @Autowired
    CropService cropService;


    @PostMapping("/crops")
    public ResponseEntity createCrop(@RequestBody CropDTO cropDTO){
        ResponseBean bean = new ResponseBean();
        try{
            LOGGER.info(CommonUtils.fromObject(cropDTO, LOGGER));
            cropService.createCrop(cropDTO, bean);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setError(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

}
