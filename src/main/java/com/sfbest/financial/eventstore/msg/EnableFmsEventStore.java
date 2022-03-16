package com.sfbest.financial.eventstore.msg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EventigurationSelector.class)
public @interface EnableFmsEventStore {

    boolean modular() default false;
}
