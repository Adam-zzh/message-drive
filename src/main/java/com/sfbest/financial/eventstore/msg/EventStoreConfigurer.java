package com.sfbest.financial.eventstore.msg;

import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.transaction.TransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;

public class EventStoreConfigurer {

    DataSource ds;

    TransactionManager tms;


    FmsEventProperties properties;

    ExecutorService taskExecutor;

    public EventStoreConfigurer(DataSource ds, TransactionManager tms, FmsEventProperties properties, ExecutorService taskExecutor) {
        this.ds = ds;
        this.tms = tms;
        this.properties = properties;
        this.taskExecutor = taskExecutor;
    }


    public MysqlFmsMessageStore createMessageStore(){
        MysqlFmsMessageStore messageStore = new MysqlFmsMessageStore(ds);
        messageStore.setPartionCount(properties.getPartionCount());
        return messageStore;
    }

    public IPublisher publisher(MysqlFmsMessageStore messageStore) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        PublisherImpl publisher = new PublisherImpl();
        publisher.setMessageStore(messageStore);
        String serializerClassName = properties.getSerializerClassName();
        if(StringUtils.hasLength(serializerClassName)){
            Serializer serializer = (Serializer)Class.forName(serializerClassName).newInstance();
            publisher.setSerializer(serializer);
        }else{
            publisher.setSerializer(new DefaultSerializer());
        }

        return publisher;
    }

    public  MessageHandler messageHandler(MysqlFmsMessageStore messageStore,SubcriberStore subcriberStore) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MessageHandler messageHandler = new MessageHandler(messageStore,subcriberStore);
        String deserializerClassName = properties.getDeserializerClassName();
        if(StringUtils.hasLength(deserializerClassName)){
            Deserializer deserializer = (Deserializer)Class.forName(deserializerClassName).newInstance();
            messageHandler.setDeserializer(deserializer);
        }
        return messageHandler;
    }


    public EventDistributionFactoryBean distribution( MysqlFmsMessageStore messageStore, MessageHandler messageHandler) throws Exception {
        EventDistributionFactoryBean distribution = new EventDistributionFactoryBean(tms,properties.getPartionCount(),messageStore,messageHandler,taskExecutor);
        return distribution;
    }

}
