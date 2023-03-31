package com.munger;

import com.munger.service.UserInterface;
import com.munger.service.UserService;
import com.spring.MungerApplicationContext;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 11:33;
 * @Params:
 * @Return:
 * @Description:
 */
public class Test {

    public static void main(String[] args) {

        MungerApplicationContext applicationContext = new MungerApplicationContext(Appconfig.class);

        UserInterface userService = (UserInterface) applicationContext.getBean("userService");
        userService.test();
    }


}
