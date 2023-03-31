package com.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Munger.;
 * @Date: 2023/3/31 13:49;
 * @Params:
 * @Return:
 * @Description:
 */
public class MungerApplicationContext {

    private Class configClass;

    private final Map<String, BeanDefination> beanDefinitionMap = new ConcurrentHashMap<>();

    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> initializeBeanPostProcessorList = new LinkedList<BeanPostProcessor>();

    public MungerApplicationContext(Class configClass) {
        this.configClass = configClass;

        scan(configClass);

        for (Map.Entry<String, BeanDefination> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefination beanDefination = entry.getValue();
            if (beanDefination.getScope().equals("singleton")) {

                Object bean = createBean(beanName, beanDefination);
                singletonObjects.put(beanName, bean);

            }

        }

    }
    public Object createBean(String beanName, BeanDefination beanDefination) {
        Class clazz = beanDefination.getType();

        Object instance = null;
        try {

            instance = clazz.getConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            for (BeanPostProcessor beanPostProcessor : initializeBeanPostProcessorList) {
                beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            }

            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            for (BeanPostProcessor beanPostProcessor : initializeBeanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            }



        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException();
        }

        BeanDefination beanDefination = beanDefinitionMap.get(beanName);
        if ("singleton".equals(beanDefination.getScope())) {

            Object singletonBean = singletonObjects.get(beanName);
            if (singletonBean == null) {
                singletonBean = createBean(beanName, beanDefination);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        } else {
            return createBean(beanName, beanDefination);

        }
    }

    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            path = path.replace(".", "/");

            ClassLoader classLoader = MungerApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());

            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));

                    absolutePath = absolutePath.replace("/", ".");
                    try {
                        Class<?> clazz = classLoader.loadClass(absolutePath);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                initializeBeanPostProcessorList.add(beanPostProcessor);

                            }

                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();
                            if ("".equals(beanName)) {
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }

                            BeanDefination beanDefination = new BeanDefination();
                            beanDefination.setType(clazz);

                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope annotation = clazz.getAnnotation(Scope.class);
                                String value = annotation.value();
                                beanDefination.setScope(value);

                            } else {
                                beanDefination.setScope("singleton");

                            }

                            beanDefinitionMap.put(beanName, beanDefination);

                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}
