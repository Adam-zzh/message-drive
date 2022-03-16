package com.sfbest.financial.eventstore.msg;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class EventDistributionImpl implements  IEventDistribution{
     Logger logger = LoggerFactory.getLogger(EventDistributionImpl.class);

     Random random = new Random();

     private int partionCount;

     private int rand ;



    private MysqlFmsMessageStore messageStore;


    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private MessageHandler messageHandler;

    public EventDistributionImpl(int partionCount, MysqlFmsMessageStore messageStore, MessageHandler messageHandler) {
        this.partionCount = partionCount;
        rand = random.nextInt(partionCount);
        this.messageStore = messageStore;
        this.messageHandler=messageHandler;
    }


    public void distribute() throws IOException {
        logger.info("轮询{}分区",rand);
        List<FmsEventMessage> list = messageStore.queryPartionNotDealMessage(rand);

        rand = (rand + 1) % 6;
        List<Future<Integer> > futureList = Lists.newLinkedList();
        for (FmsEventMessage eventMessage : list) {
            Future<Integer> f = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return messageHandler.handlerMessage(eventMessage);
                }
            });
            futureList.add(f);
        }
        for (Future<Integer> integerFuture : futureList) {
            try {
                integerFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
