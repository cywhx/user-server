package whxcy.userserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import whxcy.userserver.common.ErrorCode;
import whxcy.userserver.exceptiom.BusinessException;
import whxcy.userserver.model.domain.User;
import whxcy.userserver.service.UserService;
import whxcy.userserver.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-09-10 08:25:18
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
     private UserMapper userMapper;
    /**
     * 盐值 ，混淆密码
     */
   public static final String SALT = "yupi";
   public static final String USER_LOGIN_STATE="userLoginState";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword , String planetCode ) {
    // 1.校验
        if(StringUtils.isAnyBlank(userAccount , userPassword,  checkPassword , planetCode) ){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户过短");
        }
        if (userPassword.length() < 8 || checkPassword.length()< 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        if (planetCode.length() > 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }
    //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户包含特殊字符");
        }
        //校验密码是否相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不相同");
        }
        //校验账户是否相同
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount" , userAccount);

        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已注册");
        }
        //校验账户是否相同
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("planetCode ", planetCode);
        Long  count1= userMapper.selectCount(queryWrapper1);
        if (count1 > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球账号以被注册");
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //写入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveRegister = this.save(user);
        if (!saveRegister){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    @Override
    public User doLongin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if ( userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户过短");

        }
        if (userPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");

        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户包含特殊字符");
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User aLong = userMapper.selectOne(queryWrapper);
        if (aLong == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在或者密码错误");
        }
        //用户脱敏
        User safetyUser = getSafetyUser(aLong);
        //记录用户的状态

        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        return safetyUser;
    }

    /**
     * 注销登录
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




