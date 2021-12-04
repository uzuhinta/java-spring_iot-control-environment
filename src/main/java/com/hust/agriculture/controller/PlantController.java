package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.Plant;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.service.httpservice.PlantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
public class PlantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantController.class);

    @Autowired
    PlantService plantService;

    @RequestMapping(path = "/plants", method = RequestMethod.GET)
    public ResponseEntity getPlants(@RequestParam(name = "page", required = false) Integer page,
                                    @RequestParam(name = "size", required = false) Integer size,
                                    @RequestParam(name = "status", required = false) Integer status,
                                    @RequestParam(name = "key", required = false) String key) {

        ResponseBean bean = new ResponseBean();
        try {
            plantService.getPlants(bean, page, size, status, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @PutMapping("/plants/info")
    public ResponseEntity updateInfo(@RequestBody Plant plant){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN)){
                plantService.updateInfo(plant, bean);
            }else{
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setMessage(Constant.MSG_NOT_AUTHORIZATION);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @PostMapping("/plants/info")
    public ResponseEntity createInfo(@RequestBody Plant plant){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN)){
                plantService.createInfo(plant, bean);
            }else{
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setMessage(Constant.MSG_NOT_AUTHORIZATION);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/plants/info", method = RequestMethod.DELETE)
    public ResponseEntity changeStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {

        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN)){
                plantService.changeStatus(id, status, bean);
            }else{
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setMessage(Constant.MSG_NOT_AUTHORIZATION);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }
}
