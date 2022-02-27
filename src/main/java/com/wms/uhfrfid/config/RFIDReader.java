package com.wms.uhfrfid.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import com.wms.uhfrfid.service.rfid.reader.RFIDReaderStartup;

@Configuration
public class RFIDReader {

	private static final Logger log = LoggerFactory.getLogger(RFIDReader.class);
	private TaskExecutor taskExecutor;

	public RFIDReader(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Bean
	public void startRFIDReader() {
		RFIDReaderStartup rfidReaderStartup = RFIDReaderStartup.getInstance();

		rfidReaderStartup.startRFIDReader();
	}
}
