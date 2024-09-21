package whxcy.userserver.service;


import com.baomidou.mybatisplus.extension.service.IService;
import whxcy.userserver.model.domain.User;

import javax.servlet.http.HttpServletRequest;



/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-09-10 08:25:18
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 验证密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword ,String planetCode);

    /***
     * 用户登入
     * @param userAccount 账户
     * @param userPassword 密码
     * @param request  返回数据
     * @return 脱敏后的用户信息
     */
    User doLongin(String userAccount , String userPassword , HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 注销用户
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
