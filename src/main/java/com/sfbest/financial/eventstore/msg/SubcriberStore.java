package com.sfbest.financial.eventstore.msg;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SubcriberStore {

    private  static ConcurrentHashMap<String, List<SubcriberDefinition>> subcribersMap = new ConcurrentHashMap<>();

    public  List<SubcriberDefinition> getChannelSubcribers(String channel){
        return subcribersMap.getOrDefault(channel,new LinkedList<>());
    }

    public   void  addChannalSubcribers(String channel, Method method, Object bean){
        SubcriberDefinition definition = new SubcriberDefinition(channel,bean,method);
        subcribersMap.putIfAbsent(channel,new LinkedList<>());
        subcribersMap.get(channel).add(definition);
    }
}
