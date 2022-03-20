/**
 * 
 */
package com.wms.uhfrfid.service.rfid.reader;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hmr
 *
 */
public class RFIDTransit extends RFIDTagListener implements Runnable {
    private final Logger log = LoggerFactory.getLogger(RFIDTransit.class);

    private RFIDTag rfidTag;
	/**
	 * @param job
	 * @param consumers
	 */
	public RFIDTransit(String job, PoolRFIDConsumer consumers, Vector<Integer> antennaSet,
			int rfidTagToRead, int timeout) {
		super(job, consumers, antennaSet, rfidTagToRead, timeout);
	}

	public synchronized void onMessage(RFIDTag rfidTag) {
		try {
			log.debug("RFIDInventory: tags to read: " + rfidTagToRead + " tags read so far: " + rfidTagCount.get());
			this.rfidTag = rfidTag;
			notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				wait(0);
				log.debug("RFIDInventory: timeout: " + timeout + " total tags scanned: " + rfidTag.get_EPC());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
}