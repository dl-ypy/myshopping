package com.myshopping.service;

import com.myshopping.common.ServerResponse;
import com.myshopping.model.User;

/**
 * Created by Administrator on 2017/10/3.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);
    ServerResponse<User> register(User user);
    ServerResponse<String> checkValid(String str, String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username, String question, String answer);
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);
    ServerResponse<User> update_information(User user);
    ServerResponse<User> getInformation(Integer userId);
    ServerResponse checkAdminRole(User user);
}
