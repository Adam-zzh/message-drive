package com.sfbest.financial.eventstore.msg;


public interface PartitionStrategy {

    int patition(String key);
}
