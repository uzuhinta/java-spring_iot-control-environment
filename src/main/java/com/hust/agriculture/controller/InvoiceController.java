package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.service.httpservice.InvoiceService;
import com.hust.agriculture.service.httpservice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
public class InvoiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    UserService userService;

    @GetMapping("/active")
    public ResponseEntity getInvoice(){
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(path = "/invoices", method = RequestMethod.GET)
    public ResponseEntity getInvoices(@RequestParam(name = "page", required = false) Integer page,
                                    @RequestParam(name = "size", required = false) Integer size,
                                    @RequestParam(name = "status", required = false) Integer status,
                                    @RequestParam(name = "key", required = false) String key,
                                    @RequestParam(name = "username", required = false) String username) {

        ResponseBean bean = new ResponseBean();
        try {
            if(ObjectUtils.isEmpty(username)) {
                invoiceService.getInvoices(bean, page, size, status, key);
            }else{
                invoiceService.getInvoicesByUser(bean, status, username);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @RequestMapping(path = "/user/invoices", method = RequestMethod.GET)
    public ResponseEntity getInvoicesByUser(@RequestParam(name = "status", required = false) Integer status) {

        ResponseBean bean = new ResponseBean();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userActive = (User) authentication.getPrincipal();
        try {
            invoiceService.getInvoicesByUser(bean, status, userActive);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }
}
