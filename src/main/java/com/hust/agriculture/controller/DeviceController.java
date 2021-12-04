package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Device;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.ControlMQTT;
import com.hust.agriculture.payload.request.DataMQTT;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.service.httpservice.CachingService;
import com.hust.agriculture.service.httpservice.DeviceService;
import com.hust.agriculture.service.httpservice.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
public class DeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    DeviceService deviceService;

    @Autowired
    CachingService cachingService;

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/devices/latest")
    public ResponseEntity getLatestData(@RequestParam(name = "deviceId") Long deviceId){
        ResponseBean bean = new ResponseBean();
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(invoiceService.isHasInvoiceOverDueDate(userActive.getUsername())){
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setError(Constant.MSG_UNPAID_INVOICES);
            }else {
                DataMQTT dataMQTT = cachingService.getDataDevice(String.valueOf(deviceId));
                bean.setError(Constant.ERROR_CODE_OK);
                bean.addData("data", dataMQTT);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @GetMapping("/devices")
    public ResponseEntity getALl(@RequestParam(required = false, name = "farmId") Long farmId){
        ResponseBean bean = new ResponseBean();
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            if(farmId != null){
                deviceService.findAllByFarmId(user.getID(), farmId, bean);
            }else {
                deviceService.findAllByUserId(user.getID(), bean);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @PutMapping("/devices/info")
    public ResponseEntity updateInfo(@RequestBody Device device){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN) || deviceService.isOwnerByUser(userActive.getID(), device.getID())){
                deviceService.updateInfo(device, bean);
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

    @PostMapping("/devices/info")
    public ResponseEntity createInfo(@RequestBody Device device){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN)){
                deviceService.createInfo(device, bean);
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

    @RequestMapping(path = "/devices/info", method = RequestMethod.DELETE)
    public ResponseEntity changeStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {

        ResponseBean bean = new ResponseBean();
        try {
            deviceService.changeStatus(id, status, bean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/devices/control", method = RequestMethod.POST)
    public ResponseEntity controlDevice(@RequestBody ControlMQTT controlMQTT) {

        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(invoiceService.isHasInvoiceOverDueDate(userActive.getUsername())){
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setError(Constant.MSG_UNPAID_INVOICES);
            }else {
                deviceService.control(bean, userActive, controlMQTT);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }
}
