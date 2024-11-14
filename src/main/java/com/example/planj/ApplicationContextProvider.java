package com.example.planj;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
        System.out.println("ApplicationContext initialized: " + (context != null));
    }

    public static ApplicationContext getContext() {
        return context;
    }


}
