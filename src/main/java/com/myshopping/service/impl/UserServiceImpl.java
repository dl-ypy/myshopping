package com.myshopping.service.impl;

import com.myshopping.common.Const;
import com.myshopping.common.ServerResponse;
import com.myshopping.common.TokenCache;
import com.myshopping.dao.UserMapper;
import com.myshopping.model.User;
import com.myshopping.service.IUserService;
import com.myshopping.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/10/3.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在！");
        }

        //比较的是MD5加密后的密码
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功",user);
    }

    @Override
    public ServerResponse<User> register(User user) {
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) { //如果校验不成功，返回错误信息
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // TODO: MD5加密
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败！");
        }
        return ServerResponse.createBySuccessMessage("注册成功！");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {  //判断type是否为空
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已经存在！");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已经存在！");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误！");
        }
        return ServerResponse.createBySuccessMessage("校验成功！");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) { //如果校验成功，说明用户名不存在
            return ServerResponse.createByErrorMessage("该用户名不存在！");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的！");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //说明答案正确
            String forgetToken = UUID.randomUUID().toString();  //几乎不会重复
            // TODO: 本地缓存
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("答案错误！");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，需要传递token！");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) { //如果校验成功，说明用户名不存在
            return ServerResponse.createByErrorMessage("该用户名不存在！");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期！");
        }
        if (StringUtils.equals(token,forgetToken)) {   //通过StringUtils的equals判断可以避免空指针错误
            String MD5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,MD5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("密码修改成功！");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取！");
        }
        return ServerResponse.createByErrorMessage("修改密码失败！");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //查询旧密码
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误！");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功！");
        }
        return ServerResponse.createByErrorMessage("密码更新失败！");
    }

    @Override
    public ServerResponse<User> update_information(User user) {
        //username不能更新
        //email也要进行验证，校验新的Email是否已经存在
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在，请更换邮箱！");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setUpdateTime(new Date());     //这里传的时间只是个标志，表示设置更新时间不为空，不为空数据库中才会修改更新时间

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功！", updateUser);  //将更新后的用户类也返回去
        }
        return ServerResponse.createByErrorMessage("更新信息失败！");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户！");
        }
        user.setPassword(StringUtils.EMPTY);   //不能将密码显示到前台
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 判断是否为管理员
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user!=null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
