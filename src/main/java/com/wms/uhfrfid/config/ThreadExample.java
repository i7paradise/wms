package com.wms.uhfrfid.config;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ThreadExample {

    private static final Logger log = LoggerFactory.getLogger(ThreadExample.class);

    private class MessagePrinterTask implements Runnable {

        private String message;

        public MessagePrinterTask(String message) {
            this.message = message;
        }

        public void run() {
			while (true) {
				log.error("### The time is now {}", LocalDateTime.now());
				log.error("Message is: ", message);
				System.out.println(message);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
    }

    private TaskExecutor taskExecutor;

    public ThreadExample(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Bean
    public void printMessages() {
//        for(int i = 0; i < 25; i++) {
            taskExecutor.execute(new MessagePrinterTask("Message SALAM" ));
//        }
    }

//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//        log.error("### The time is now {}", LocalDateTime.now());
//	}

}
