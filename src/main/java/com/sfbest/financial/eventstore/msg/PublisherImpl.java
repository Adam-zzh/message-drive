package com.sfbest.financial.eventstore.msg;

import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class PublisherImpl implements IPublisher{

    MysqlFmsMessageStore messageStore;

    Serializer serializer = new DefaultSerializer();

    PartitionStrategy partitionStrategy = new GeneralPatitionStrategy(6);

    public void publish(String channel,Object event,String key) throws IOException {
        byte[]buf = serializer.serializeToByteArray(event);
        FmsEventMessage eventMessage = new FmsEventMessage();
        eventMessage.setEventChannel(channel);
        eventMessage.setMessageBytes(buf);
        eventMessage.setMessageId(UUID.randomUUID().toString().replaceAll("-",""));
        eventMessage.setCreateTime(new Date());
        eventMessage.setMessageStatus(0);
        eventMessage.setMessageKey(key);
        eventMessage.setPartitionId(partitionStrategy.patition(key));
        partitionStrategy.patition(key);
        messageStore.addMessage(eventMessage);
    }

    @Override
    public void publish(String channel, Object event) throws IOException {
        String key = UUID.randomUUID().toString().replaceAll("-","");
        publish(channel,event,key);
    }

    public void setMessageStore(MysqlFmsMessageStore messageStore) {
        this.messageStore = messageStore;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }
}
