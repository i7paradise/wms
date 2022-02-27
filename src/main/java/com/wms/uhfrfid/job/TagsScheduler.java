package com.wms.uhfrfid.job;

import com.rfidread.RFIDReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TagsScheduler {

    private static final Logger log = LoggerFactory.getLogger(TagsScheduler.class);

    public void scan() {
        String s = RFIDReader.Read_EPC("", "");
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("### The time is now {}", LocalDateTime.now());
    }
}
