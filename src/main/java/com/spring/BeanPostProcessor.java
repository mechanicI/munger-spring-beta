package com.spring;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 15:50;
 * @Params:
 * @Return:
 * @Description:
 */
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
