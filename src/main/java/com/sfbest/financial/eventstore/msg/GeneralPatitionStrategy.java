package com.sfbest.financial.eventstore.msg;


public class GeneralPatitionStrategy implements PartitionStrategy{



    int totalPartitionCount;

    public GeneralPatitionStrategy(int totalPartitionCount) {
        this.totalPartitionCount = totalPartitionCount;
    }

    @Override
    public int patition(String key) {
        return Math.abs(key.hashCode())%totalPartitionCount;
    }


    public void setTotalPartitionCount(int totalPartitionCount) {
        this.totalPartitionCount = totalPartitionCount;
    }
}
