package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.payload.request.AccountCredentials;
import com.hust.agriculture.payload.request.PaymentDTO;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.service.httpservice.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    DeviceService deviceService;

    @Autowired
    UserService userService;

    @Autowired
    FarmService farmService;

    @Autowired
    PlantService plantService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    CropService cropService;

    @RequestMapping(path = "/invoices/generate", method = RequestMethod.GET)
    public ResponseEntity generateInvoices(){
        ResponseBean bean = new ResponseBean();
        try {
            invoiceService.scheduleCreateInvoice();
            bean.setError(Constant.ERROR_CODE_OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/pay", method = RequestMethod.POST)
    public ResponseEntity pay(@RequestBody PaymentDTO paymentDTO){
        ResponseBean bean = new ResponseBean();
        try {
            paymentService.paid(paymentDTO, bean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(value = "/device/properties", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getPropertiesData(@RequestParam Long deviceId) {
        try {
            ByteArrayInputStream byteArrayInputStream = deviceService.getData(deviceId);
            return IOUtils.toByteArray(byteArrayInputStream);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }


    @RequestMapping(path = "/device", method = RequestMethod.POST)
    public ResponseEntity download(String deviceName) {

        ResponseBean bean = new ResponseBean();
        try {
            deviceService.createDevice(deviceName, bean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/crops", method = RequestMethod.GET)
    public ResponseEntity getCrops(@RequestParam(name = "page", required = false) Integer page,
                                      @RequestParam(name = "size", required = false) Integer size,
                                      @RequestParam(name = "status", required = false) Integer status,
                                      @RequestParam(name = "key", required = false) String key) {

        ResponseBean bean = new ResponseBean();
        try {
            cropService.getCrops(bean, page, size, status, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/invoices", method = RequestMethod.GET)
    public ResponseEntity getInvoices(@RequestParam(name = "page", required = false) Integer page,
                                   @RequestParam(name = "size", required = false) Integer size,
                                   @RequestParam(name = "status", required = false) Integer status,
                                   @RequestParam(name = "key", required = false) String key) {

        ResponseBean bean = new ResponseBean();
        try {
            invoiceService.getInvoices(bean, page, size, status, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }


    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ResponseEntity getUsers(@RequestParam(name = "page", required = false) Integer page,
                                 @RequestParam(name = "size", required = false) Integer size,
                                 @RequestParam(name = "status", required = false) Integer status,
                                 @RequestParam(name = "key", required = false) String key) {

        ResponseBean bean = new ResponseBean();
        try {
            userService.getAll(bean, page, size, status, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/farms", method = RequestMethod.GET)
    public ResponseEntity getFarms(@RequestParam(name = "page", required = false) Integer page,
                                 @RequestParam(name = "size", required = false) Integer size,
                                 @RequestParam(name = "status", required = false) Integer status,
                                 @RequestParam(name = "key", required = false) String key) {

        ResponseBean bean = new ResponseBean();
        try {
            farmService.getFarms(bean, page, size, status, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/devices", method = RequestMethod.GET)
    public ResponseEntity getDevices(@RequestParam(name = "page", required = false) Integer page,
                                   @RequestParam(name = "size", required = false) Integer size,
                                   @RequestParam(name = "status", required = false) Integer status,
                                   @RequestParam(name = "key", required = false) String key) {

        ResponseBean bean = new ResponseBean();
        try {
            deviceService.getDevices(bean, page, size, status, key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/users/reset_pass", method = RequestMethod.POST)
    public ResponseEntity resetPass(@RequestBody AccountCredentials account) {

        ResponseBean bean = new ResponseBean();
        try {
            userService.resetPass(account, bean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/users/change_status", method = RequestMethod.DELETE)
    public ResponseEntity changeStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {

        ResponseBean bean = new ResponseBean();
        try {
            userService.changeStatus(id, status, bean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

}
