package com.sfbest.financial.eventstore.msg;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MessageHandler {

    Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    MysqlFmsMessageStore messageStore;
    SubcriberStore subcriberStore;
    Deserializer deserializer = new DefaultDeserializer();

    public MessageHandler(MysqlFmsMessageStore messageStore, SubcriberStore subcriberStore) {
        this.messageStore = messageStore;
        this.subcriberStore = subcriberStore;
    }

    public Integer handlerMessage(FmsEventMessage eventMessage ) throws IOException, InvocationTargetException, IllegalAccessException {
        try{


        InputStream in = new ByteArrayInputStream(eventMessage.getMessageBytes());

        Object o = deserializer.deserialize(in);
        List<SubcriberDefinition> subcriberDefinitionList = subcriberStore.getChannelSubcribers(eventMessage.getEventChannel());
        for (SubcriberDefinition definition : subcriberDefinitionList) {
           // logger.info("messageId:{} 对应的 处理方法为：{}",eventMessage.getMessageId(),definition);
            //definition.getMethod().invo
            definition.getMethod().invoke(definition.getBean(),o);
        }
        messageStore.updateMessageDealed(eventMessage.getMessageId());
        }catch (Exception e){
            e.printStackTrace();
        }
        //更新为以完成状态
        return 0;
    }


    public void setDeserializer(Deserializer deserializer) {
        this.deserializer = deserializer;
    }
}
