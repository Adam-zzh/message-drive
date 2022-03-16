package com.sfbest.financial.eventstore.msg;

import java.io.IOException;

public interface IPublisher {
    void publish(String channel,Object event,String key) throws IOException;

    void publish(String channel,Object event) throws IOException;
}
