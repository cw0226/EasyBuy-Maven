package cn.easybuy.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtils {
    private static ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static Object getBean(String beanId){
       return applicationContext.getBean(beanId);
    }
}
