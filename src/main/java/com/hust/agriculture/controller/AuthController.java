package com.hust.agriculture.controller;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.Account;
import com.hust.agriculture.payload.request.AccountCredentials;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.security.JwtTokenProvider;
import com.hust.agriculture.service.httpservice.InvoiceService;
import com.hust.agriculture.service.httpservice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @Autowired
    InvoiceService invoiceService;

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody AccountCredentials credentials){
        ResponseBean bean = new ResponseBean();
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            Collections.emptyList()
                    )
            );
            SimpleGrantedAuthority sga = (SimpleGrantedAuthority) authentication.getAuthorities().iterator().next();
            String role = sga.getAuthority();
            String jwt = jwtTokenProvider.generateUserToken(credentials.getUsername(), role);
            String topicName = userService.generateTopicName(credentials.getUsername());

            if(invoiceService.isHasInvoiceOverDueDate(credentials.getUsername())){
                bean.addData("alert", Constant.MSG_UNPAID_INVOICES);
            }

            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_SIGN_IN_OK);
            bean.addData("jwt", jwt);
            bean.addData("topicName", topicName);
            bean.addData("roles", role);
        } catch (BadCredentialsException e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SIGN_IN_NOK);
        } catch (DisabledException e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_USER_NOT_ACTIVE);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody Account account){
        ResponseBean bean = new ResponseBean();
        try{
            if(account != null) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                User userActive = (User) authentication.getPrincipal();
//                if(userActive == null ||userActive.getRole().getID() != Constant.ROLE_USER_ADMIN){
//                    account.setRoleType(null);
//                }
                userService.signUp(account, bean);
                bean.setError(Constant.ERROR_CODE_OK);
            }else{
                bean.setMessage(Constant.MSG_INPUT_INVALID);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            bean.setMessage(Constant.MSG_SERVER_ERROR);
        }
        return ResponseEntity.ok(bean);
    }
}
