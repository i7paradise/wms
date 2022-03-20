package com.wms.uhfrfid.config;

import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import com.wms.uhfrfid.service.rfid.reader.RFIDInventory;
import com.wms.uhfrfid.service.rfid.reader.RFIDReaderStartup;
import com.wms.uhfrfid.service.rfid.reader.RFIDTag;
import com.wms.uhfrfid.service.rfid.reader.RFIDTransit;

@Configuration
public class RFIDReader {

	private static final int TIMEOUT = 1000;
	private static final Logger log = LoggerFactory.getLogger(RFIDReader.class);
	private TaskExecutor taskExecutor;

	public RFIDReader(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Bean
	public void startRFIDReader() {
		RFIDReaderStartup rfidReaderStartup = RFIDReaderStartup.getInstance();

		rfidReaderStartup.startRFIDReader();

        Map<String, RFIDTag> result;
        Vector<Integer> antennaSet = new Vector();
        antennaSet.add(3);
        RFIDTransit rfidTransit = new RFIDTransit("     RFID Transit listener ==========> ",
        		RFIDReaderStartup.getInstance().getConsumers(),
                antennaSet, 1, TIMEOUT);
	}
}
