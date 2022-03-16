package com.sfbest.financial.eventstore.msg;

import java.lang.reflect.Method;

public class SubcriberDefinition {

    String channel;

    Object bean;

    Method method;


    public SubcriberDefinition(String channel, Object bean, Method method) {
        this.channel = channel;
        this.bean = bean;
        this.method = method;
    }


    @Override
    public String toString() {
        return "FmsSubcriberDefinition{" +
                "channel='" + channel + '\'' +
                ", bean=" + bean +
                ", method=" + method +
                '}';
    }

    public String getChannel() {
        return channel;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}
