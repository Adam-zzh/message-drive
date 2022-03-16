package com.sfbest.financial.eventstore.msg;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Component
public class SubcriberRegister implements BeanPostProcessor {


    static SubcriberStore subcriberStore = new SubcriberStore();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = ClassUtils.getUserClass(bean.getClass());
        FmsEventListener listener = beanClass.getAnnotation(FmsEventListener.class);
        if(listener != null){
            ReflectionUtils.doWithMethods(bean.getClass(),method -> doMethod(method,bean));
        }
        return bean;
    }

    private void doMethod(Method method,Object bean){
        Subcriber subcriber = method.getAnnotation(Subcriber.class);
        if(subcriber != null ){
            String channel = subcriber.channal();
            subcriberStore.addChannalSubcribers(channel,method,bean);
        }
    }


}
