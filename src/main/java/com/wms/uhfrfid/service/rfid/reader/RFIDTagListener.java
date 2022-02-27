/**
 * 
 */
package com.wms.uhfrfid.service.rfid.reader;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hmr
 *
 */
public class RFIDTagListener implements AutoCloseable {
	protected PoolRFIDConsumer consumers = null;
	protected String job = null;
	protected Vector<Integer> antennaSet = null;
	protected HashMap<String, RFIDTag> rfidTagSet = null;
	protected AtomicInteger rfidTagCount = new AtomicInteger();
	protected int rfidTagToRead = 0;
	protected int timeout = 0;
    private final Logger log = LoggerFactory.getLogger(RFIDTagListener.class);
	
	/**
	 * @param consumers
	 */
	public RFIDTagListener(String job, PoolRFIDConsumer consumers, Vector<Integer> antennaSet,
			int rfidTagToRead, int timeout) {
		this.job = job;
		this.consumers = consumers;
		this.antennaSet = antennaSet;
		this.rfidTagSet = new HashMap<String, RFIDTag>();
		this.rfidTagToRead = rfidTagToRead;
		this.timeout = timeout;

		log.debug(this.toString() + " registring listenners linked to those antenna: " + this.antennaSet.toString());
		for (Integer antenna : this.antennaSet)
			this.consumers.registerListener(antenna, this);
	}

	public synchronized void onMessage(RFIDTag rfidTag) {
		try {
			log.debug("RFIDTagListener : " + job + " Antenna: " + rfidTag.get_ANT_NUM() + " # " + rfidTag.get_EPC());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws Exception {
		log.debug(this.toString() + " unregistring listenners linked to those antenna: " + this.antennaSet.toString());
		for (Integer antenna : this.antennaSet)
			this.consumers.unregisterListener(antenna, this);
	}
}