package com.spring;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 14:45;
 * @Params:
 * @Return:
 * @Description:
 */
public class BeanDefination {
    private Class type;
    private String scope;
    private boolean isLazy;

    public Class getType() {
        return type;
    }
    public void setType(Class type) {
        this.type = type;
    }
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public boolean isLazy() {
        return isLazy;
    }
    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

}
