package com.sfbest.financial.eventstore.msg;

import com.google.common.collect.Maps;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class EventDistributionFactoryBean implements FactoryBean<IEventDistribution>, InitializingBean {

    private TransactionManager transactionManager;

    ProxyFactory proxyFactory;

    private int partionCount;

    private MysqlFmsMessageStore messageStore;

    private MessageHandler messageHandler;

    private ExecutorService executor ;

    public EventDistributionFactoryBean(TransactionManager transactionManager, int partionCount, MysqlFmsMessageStore messageStore, MessageHandler messageHandler, ExecutorService executor) {
        this.transactionManager = transactionManager;
        this.partionCount = partionCount;
        this.messageStore = messageStore;
        this.messageHandler = messageHandler;
        this.executor = executor;
    }



    @Override
    public IEventDistribution getObject() throws Exception {
        if(proxyFactory == null)
            afterPropertiesSet();
        return (IEventDistribution)proxyFactory.getProxy(this.getClass().getClassLoader());
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(proxyFactory == null){
            proxyFactory = new ProxyFactory();
            TransactionAttribute transactionAttribute = new DefaultTransactionAttribute();

            NameMatchTransactionAttributeSource transactionAttributeSource = new NameMatchTransactionAttributeSource();
            Map<String, TransactionAttribute> nameMap = Maps.newHashMap();
            nameMap.put("distribute*",transactionAttribute);
            transactionAttributeSource.setNameMap(nameMap);
            TransactionInterceptor advice = new TransactionInterceptor(transactionManager, transactionAttributeSource);


            proxyFactory.addAdvice(advice);
            proxyFactory.setProxyTargetClass(false);
            proxyFactory.addInterface(IEventDistribution.class);
            EventDistributionImpl edi = new EventDistributionImpl(partionCount,messageStore,messageHandler);
            edi.setExecutor(executor);
            proxyFactory.setTarget(edi);
        }
    }
}
