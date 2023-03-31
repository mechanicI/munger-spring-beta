package com.munger.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 15:52;
 * @Params:
 * @Return:
 * @Description:
 */
@Component
public class MungerBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(MungerValue.class)) {
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean,declaredField.getAnnotation(MungerValue.class).value());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if(beanName.equals("userService")){
            Object proxyInstance = Proxy.newProxyInstance(MungerBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    System.out.println("AOP");

                    return method.invoke(bean);
                }
            });
            return proxyInstance;

        }
        return bean;
    }

}
