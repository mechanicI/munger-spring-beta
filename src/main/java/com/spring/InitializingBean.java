package com.spring;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 15:46;
 * @Params:
 * @Return:
 * @Description:
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;

}
