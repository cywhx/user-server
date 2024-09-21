package whxcy.userserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import whxcy.userserver.common.BaseResponse;
import whxcy.userserver.common.ErrorCode;
import whxcy.userserver.common.ResultUtils;
import whxcy.userserver.exceptiom.BusinessException;
import whxcy.userserver.model.domain.User;
import whxcy.userserver.model.domain.request.UserLoginRequest;
import whxcy.userserver.model.domain.request.UserRegisterRequest;
import whxcy.userserver.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static whxcy.userserver.constant.UserConstant.ADMIN_ROLE;
import static whxcy.userserver.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 参数
     * @return 用户id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long register = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(register);
    }

    /**
     *  用户登录
     * @param userLoginRequest 参数
     * @param request 返回值
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        User user = userService.doLongin(userAccount, userPassword, request);
        return ResultUtils.success(user);

    }

    /**
     * 查询用户
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if (currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        //todo 校验用户合法性
        User user = userService.getById(userId);

        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 注销用户
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        int userLogout = userService.userLogout(request);
        return ResultUtils.success(userLogout);
    }

    /**
     *查询用户
     * @param userName 用户名字
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String userName , HttpServletRequest request){
        if(!isAdmin(request)){
            throw  new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(userName)){
            queryWrapper.like("userName",userName);
        }
        List<User> list = userService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    /**
     * 删除用户
     * @param id 用户id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id ,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if(id >=0){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        boolean byId = userService.removeById(id);
        return ResultUtils.success(byId);
    }

    /**
     * 校验用户权限
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;

    }

}

