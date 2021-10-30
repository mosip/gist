package io.mosip.print.listener;

import io.mosip.print.listener.activemq.ActiveMQListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
public class PrintListenerApplication {

    @Bean
    public ThreadPoolTaskScheduler getTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(PrintListenerApplication.class, args);
        configurableApplicationContext.getBean(ActiveMQListener.class).runQueue();
    }
}
