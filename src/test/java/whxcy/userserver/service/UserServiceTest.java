package whxcy.userserver.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import whxcy.userserver.model.domain.User;

import javax.annotation.Resource;

/**
 * 用户测试
 * */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void teesAdduser() {
        User user = new User();
        user.setUsername("admin");
        user.setUserAccount("12345678");
        user.setGender(0);
        user.setUserPassword("12345678");
        user.setEmail("1");
        user.setPhone("1");

        boolean save = userService.save(user);

    }
}

//    @Test
//    void userRegister() {
//        String userAccount = "yupi";
//        String userPassword = "";
//        String checkPassword = "123456";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "yu";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "yupi";userPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "yu pi";
//        userPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "whxcy";checkPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1, result);
//
//        userAccount = "yupi";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result > 0);}
//}