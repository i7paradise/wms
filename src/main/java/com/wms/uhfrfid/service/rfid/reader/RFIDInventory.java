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
public class RFIDInventory extends RFIDTagListener implements Callable<HashMap<String, RFIDTag>> {
    private final Logger log = LoggerFactory.getLogger(RFIDInventory.class);

	/**
	 * @param job
	 * @param consumers
	 */
	public RFIDInventory(String job, PoolRFIDConsumer consumers, Vector<Integer> antennaSet,
			int rfidTagToRead, int timeout) {
		super(job, consumers, antennaSet, rfidTagToRead, timeout);
	}

	public synchronized void onMessage(RFIDTag rfidTag) {
		try {
			log.debug("RFIDInventory: tags to read: " + rfidTagToRead + " tags read so far: " + rfidTagCount.get());
			if (rfidTagCount.get() < rfidTagToRead) {
				RFIDTag rfidTagLookup = rfidTagSet.get(rfidTag.get_EPC());
				if (rfidTagLookup == null) {
					rfidTag.setReadCount(0);
					rfidTagSet.put(rfidTag.get_EPC(), rfidTag);
					rfidTagCount.incrementAndGet();
				} else {
					rfidTagLookup.incReadCount();
				}
			} else
				notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized HashMap<String, RFIDTag> call() throws Exception {
		while (true) {
			try {
				wait(timeout);
				log.debug("RFIDInventory: timeout: " + timeout + " total tags scanned: " + rfidTagSet.size());
				return rfidTagSet;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}