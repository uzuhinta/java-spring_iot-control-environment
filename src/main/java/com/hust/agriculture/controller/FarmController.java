package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.service.httpservice.FarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
public class FarmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FarmController.class);

    @Autowired
    FarmService farmService;

    @GetMapping("/farms")
    public ResponseEntity getALl() {
        ResponseBean bean = new ResponseBean();
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            farmService.findAllByUserId(user.getID(), bean);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @GetMapping("/farms/search")
    public ResponseEntity getByName(@RequestParam(name = "page", required = false) Integer page,
                                    @RequestParam(name = "size", required = false) Integer size,
                                    @RequestParam(name = "status", required = false) Integer status,
                                    @RequestParam(name = "key", required = false) String key) {
        ResponseBean bean = new ResponseBean();
        try {
            farmService.getFarmsByName(bean, page, size, status, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @PutMapping("/farms/info")
    public ResponseEntity updateInfo(@RequestBody Farm farm){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN) || userActive.getID() == farm.getUser().getID()){
                farmService.updateInfo(farm, bean);
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

    @PostMapping("/farms/info")
    public ResponseEntity createInfo(@RequestBody Farm farm){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN) || userActive.getID() == farm.getUser().getID()){
                farmService.createInfo(farm, bean);
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

    @RequestMapping(path = "/farms/info", method = RequestMethod.DELETE)
    public ResponseEntity changeStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {

        ResponseBean bean = new ResponseBean();
        try {
            farmService.changeStatus(id, status, bean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }
}
