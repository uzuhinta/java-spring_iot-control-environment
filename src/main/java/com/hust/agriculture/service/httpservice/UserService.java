package com.hust.agriculture.service.httpservice;

import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.Account;
import com.hust.agriculture.payload.request.AccountCredentials;
import com.hust.agriculture.payload.request.ChangePassDTO;
import com.hust.agriculture.payload.response.ResponseBean;

public interface UserService {

    void signUp(Account account, ResponseBean bean) throws Exception;

    String generateTopicName(String username);

    void getAll(ResponseBean bean, Integer page, Integer size, Integer status, String key) throws Exception;

    void resetPass(AccountCredentials account, ResponseBean bean) throws Exception;

    void changePass(User user, ChangePassDTO changePassDTO, ResponseBean bean) throws Exception;

    void updateInfo(User user, ResponseBean bean) throws Exception;

    void changeStatus(Long userId, Integer status, ResponseBean bean) throws Exception;
}
