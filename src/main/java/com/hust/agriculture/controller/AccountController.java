package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.AccountCredentials;
import com.hust.agriculture.payload.request.ChangePassDTO;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.service.httpservice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/secure")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    UserService userService;



    @GetMapping("/users/info")
    public ResponseEntity getInfo(){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            bean.setError(Constant.ERROR_CODE_OK);
            bean.addData("roles", Arrays.asList(user.getRole().getName()));
            bean.addData("name", user.getFullName());
            bean.addData("username", user.getUsername());
            bean.addData("email", user.getEmail());
            bean.addData("phone", user.getPhoneNumber());
            bean.addData("address", user.getAddress());
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @PutMapping("/users/info")
    public ResponseEntity updateInfo(@RequestBody User user){
        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userActive = (User) authentication.getPrincipal();
            if(userActive.getRole().getID().equals(Constant.ROLE_USER_ADMIN) || userActive.getID() == user.getID()){
                userService.updateInfo(user, bean);
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

    @RequestMapping(path = "/users/change_pass", method = RequestMethod.POST)
    public ResponseEntity changePass(@RequestBody ChangePassDTO changePassDTO) {

        ResponseBean bean = new ResponseBean();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            userService.changePass(user, changePassDTO, bean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }
}
