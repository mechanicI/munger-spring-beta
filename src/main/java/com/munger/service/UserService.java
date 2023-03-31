package com.munger.service;

import com.spring.*;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 14:00;
 * @Params:
 * @Return:
 * @Description:
 */
@Component
public class UserService implements UserInterface,BeanNameAware{

    @Autowired
    private OrderService orderService;


    @MungerValue("this is munger value")
    private String mungerValue;


    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void test(){

        System.out.println(orderService);
        System.out.println(mungerValue);
        System.out.println(beanName);
    }


}
