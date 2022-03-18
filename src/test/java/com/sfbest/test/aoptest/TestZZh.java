package com.sfbest.test.aoptest;

import com.sfbest.financial.eventstore.msg.IEventDistribution;
import com.sfbest.financial.eventstore.MessageApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈测试〉
 *
 * @author ZZH
 * @create 2022/3/18
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE, classes = MessageApplication.class)
public class TestZZh {

    @Autowired
    private BeanFactory beanFactory;

    @Test
    public void test1() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                IEventDistribution distribution = (IEventDistribution)beanFactory.getBean("distribution");
                System.out.println(distribution);
            });
        }

        while (!executorService.isShutdown()){
            TimeUnit.SECONDS.sleep(2);
        }

    }
}