package com.sfbest.financial.eventstore.msg;

import java.io.IOException;

public interface IEventDistribution {

    public void distribute() throws IOException;
}
