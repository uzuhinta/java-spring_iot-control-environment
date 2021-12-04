package com.hust.agriculture.service.httpservice.impl;

import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.Device;
import com.hust.agriculture.model.Role;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.Account;
import com.hust.agriculture.payload.request.AccountCredentials;
import com.hust.agriculture.payload.request.ChangePassDTO;
import com.hust.agriculture.payload.response.ResponseBean;
import com.hust.agriculture.repository.DeviceRepository;
import com.hust.agriculture.repository.RoleRepository;
import com.hust.agriculture.repository.UserRepository;
import com.hust.agriculture.service.httpservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public void signUp(Account account, ResponseBean bean) throws Exception {
        if(isValidAccount(account, bean)){
            User user = userRepository.findByUsernameOrEmailOrPhoneNumber(account.getUsername(),
                    account.getEmail(),
                    account.getPhoneNumber());
            if(user == null){
                user = new User();
                user.setUsername(account.getUsername());
                user.setPassword(passwordEncoder.encode(account.getPassword()));
                user.setAddress(account.getAddress());
                user.setFullName(account.getFullName());
                user.setEmail(account.getEmail());
                user.setPhoneNumber(account.getPhoneNumber());

                if(account.getRoleType() != null && account.getRoleType() == Constant.ROLE_USER_ADMIN){
                    user.setStatus(Constant.STATUS_ACTIVE);
                    Role role = roleRepository.findById(Constant.ROLE_USER_ADMIN).orElse(null);
                    user.setRole(role);
                }else {
                    user.setStatus(Constant.STATUS_ACTIVE);
                    Role role = roleRepository.findById(Constant.ROLE_USER_ID).orElse(null);
                    user.setRole(role);
                }

                userRepository.saveAndFlush(user);
                bean.setMessage(Constant.MSG_SIGN_UP_OK);
            }else{
                bean.setMessage(Constant.MSG_USER_EXIST);
            }
        }else{
            bean.setMessage(Constant.MSG_INPUT_INVALID);
        }
    }

    @Override
    public String generateTopicName(String username) {
        User user;
        Device device;
        String topicName;

        do{
            UUID uuid = UUID.randomUUID();
            topicName = Constant.DATA_TOPIC+uuid.toString();
            user = userRepository.findByTopicName(topicName);
            device = deviceRepository.findByTopicName(topicName);
        }while (user != null || device != null);

        user = userRepository.findByUsername(username);
        if(user != null){
            user.setTopicName(topicName);
            userRepository.saveAndFlush(user);
            return topicName;
        }
        return null;
    }

    @Override
    public void getAll(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception {
        if(ObjectUtils.isEmpty(page) || ObjectUtils.isEmpty(size)) {
            List<User> users = userRepository.findAllCustomer(status);
            bean.addData("users", users);
        }else{
            Pageable pageable = PageRequest.of(page-1, size);
            Page<User> pages = userRepository.findCustomer(pageable, status, key);
            bean.addData("total", pages.getTotalElements());
            bean.addData("items", pages.getContent());
        }
        bean.setError(Constant.ERROR_CODE_OK);
    }

    @Override
    public void resetPass(AccountCredentials account, ResponseBean bean) throws Exception {
        User user = userRepository.findByUsername(account.getUsername());
        if(user != null){
            user.setPassword(passwordEncoder.encode(account.getPassword()));
            userRepository.saveAndFlush(user);
            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_RESET_PASS_OK);
        }else{
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
        }
    }

    @Override
    public void changePass(User user, ChangePassDTO changePassDTO, ResponseBean bean) throws Exception {
        if(isValidPass(changePassDTO, bean)){
            if(passwordEncoder.matches(changePassDTO.getOldPass(), user.getPassword())){
                user.setPassword(passwordEncoder.encode(changePassDTO.getNewPass()));
                userRepository.saveAndFlush(user);
                bean.setError(Constant.ERROR_CODE_OK);
                bean.setMessage(Constant.MSG_CHANGE_PASS_SUCCESS);
            }else{
                bean.setError(Constant.ERROR_CODE_NOK);
                bean.setMessage(Constant.MSG_PASS_INVALID);
            }
        }
    }

    @Override
    public void updateInfo(User user, ResponseBean bean) throws Exception {
        userRepository.saveAndFlush(user);
        bean.setError(Constant.ERROR_CODE_OK);
        bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
    }

    @Override
    public void changeStatus(Long userId, Integer status, ResponseBean bean) throws Exception {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.setStatus(status);
            userRepository.saveAndFlush(user);
            bean.setError(Constant.ERROR_CODE_OK);
            bean.setMessage(Constant.MSG_UPDATE_INFO_SUCCESS);
        }else{
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_USER_NOT_EXISTS);
        }
    }

    private boolean isValidPass(ChangePassDTO changePassDTO, ResponseBean bean){
        if(ObjectUtils.isEmpty(changePassDTO.getOldPass())){
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_OLD_PASS_INVALID);
            return false;
        }
        if(ObjectUtils.isEmpty(changePassDTO.getNewPass())){
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_NEW_PASS_INVALID);
            return false;
        }
        if(ObjectUtils.isEmpty(changePassDTO.getConfirmPass())){
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_CONFIRM_PASS_INVALID);
            return false;
        }
        if(!changePassDTO.getNewPass().equals(changePassDTO.getConfirmPass())){
            bean.setError(Constant.ERROR_CODE_NOK);
            bean.setMessage(Constant.MSG_CONFIRM_PASS_INVALID);
            return false;
        }

        return true;
    }

    private boolean isValidAccount(Account account, ResponseBean bean){
        if(account.getUsername() == null){
            return false;
        }
        if(account.getPassword() == null){
            return false;
        }
        if(account.getEmail() == null){
            return false;
        }
        if(account.getPhoneNumber() == null){
            return false;
        }
        if(account.getAddress() == null){
            return false;
        }
        if(account.getFullName() == null){
            return false;
        }
        return true;
    }
}
