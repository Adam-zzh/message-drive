package com.sfbest.financial.eventstore.msg;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "fms.event"
)
public class FmsEventProperties {

    int partionCount;

    String serializerClassName;

    String deserializerClassName;


    public int getPartionCount() {
        return partionCount;
    }

    public void setPartionCount(int partionCount) {
        this.partionCount = partionCount;
    }

    public String getSerializerClassName() {
        return serializerClassName;
    }

    public void setSerializerClassName(String serializerClassName) {
        this.serializerClassName = serializerClassName;
    }

    public String getDeserializerClassName() {
        return deserializerClassName;
    }

    public void setDeserializerClassName(String deserializerClassName) {
        this.deserializerClassName = deserializerClassName;
    }
}
