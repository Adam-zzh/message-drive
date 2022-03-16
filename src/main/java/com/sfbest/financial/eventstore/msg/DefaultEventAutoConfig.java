package com.sfbest.financial.eventstore.msg;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties(FmsEventProperties.class)
public class DefaultEventAutoConfig implements BeanFactoryAware {

    @Autowired(required = false)
    DataSource ds;

    @Autowired(required = false)
    TransactionManager tms;

    @Autowired(required = false)
    ExecutorService taskExecutor;

    @Autowired
    FmsEventProperties eventProperties;


    EventStoreConfigurer configurer;

    BeanFactory beanFactroy;



    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        if(ds == null){
            throw new Exception("con not open datasouce");
        }
        tms = new DataSourceTransactionManager(ds);
        if(taskExecutor == null){
            taskExecutor = Executors.newCachedThreadPool();
        }
        configurer = new EventStoreConfigurer(ds,tms,eventProperties,taskExecutor);
    }


    @Bean
    public MysqlFmsMessageStore createMessageStore(FmsEventProperties properties, DataSource ds){
        return configurer.createMessageStore();
    }

    @Bean
    public IPublisher publisher(MysqlFmsMessageStore messageStore) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      return configurer.publisher(messageStore);
    }

    @Bean
    public SubcriberStore subcriberStore(){
        return new SubcriberStore();
    }

    @Bean
    public  MessageHandler messageHandler(MysqlFmsMessageStore messageStore,SubcriberStore subcriberStore) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
       return configurer.messageHandler(messageStore,subcriberStore);
    }


    @Bean
    public EventDistributionFactoryBean distribution( MysqlFmsMessageStore messageStore, MessageHandler messageHandler) throws Exception {

        return configurer.distribution(messageStore,messageHandler);
    }


    @Scheduled(fixedDelay=2000)
    public void eventTask()  {
        try {
            IEventDistribution distribution = (IEventDistribution)beanFactroy.getBean("distribution");
            distribution.distribute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactroy=beanFactory;
    }
}
