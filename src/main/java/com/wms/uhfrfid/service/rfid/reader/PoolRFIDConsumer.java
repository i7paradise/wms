/**
 * 
 */
package com.wms.uhfrfid.service.rfid.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.jms.JMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hmr
 *
 */
public class PoolRFIDConsumer {
	private String brokerStorePrefix;
	private String brokerURL;
	HashMap<Integer, Integer> antennaSet;
	HashMap<Integer, Vector<RFIDConsumer>> antennaConsumers;
    private final Logger log = LoggerFactory.getLogger(PoolRFIDConsumer.class);

	/**
	 * @param poolRFIDConsumer
	 */
	public PoolRFIDConsumer(String brokerStorePrefix, String brokerURL, HashMap<Integer, Integer> antennaSet) {
		this.antennaSet = antennaSet;
		this.brokerStorePrefix = brokerStorePrefix;
		this.brokerURL = brokerURL;
	}

	boolean startRFIDConsumers() {
		if (antennaSet == null)
			return false;

		try {
			antennaConsumers = new HashMap<Integer, Vector<RFIDConsumer>>();
    		for (Map.Entry<Integer, Integer> set : antennaSet.entrySet()) {
    			Vector<RFIDConsumer> rfidConsumers = new Vector<RFIDConsumer>(set.getValue());
    			for (int cnt = 0; cnt < set.getValue(); cnt++) {
    				RFIDConsumer rfidConsumer = new RFIDConsumer(brokerStorePrefix, brokerURL, set.getKey());
    				rfidConsumers.add(rfidConsumer);
    				Thread rfidConsumerThread = new Thread(rfidConsumer);
    				rfidConsumerThread.start();
    			}
    			antennaConsumers.put(set.getKey(), rfidConsumers);
    		}

		} catch (JMSException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

    public void registerListener(Integer antenna, RFIDTagListener _rfidListener) {
		log.debug(this.toString() + " registring listenners (" + _rfidListener.toString() + ") linked to those antenna: " + this.antennaSet.toString());
		for (Map.Entry<Integer, Vector<RFIDConsumer>> set : antennaConsumers.entrySet()) {
			if (set.getKey() == antenna) {
				Vector<RFIDConsumer> rfidConsumers = set.getValue();
				//TODO: register the rfid listener in round-robin fashion was to the set of rfid consumers
				for (RFIDConsumer rfidConsumer : rfidConsumers)
					rfidConsumer.registerListener(_rfidListener);
			}
		}
    }

    public void unregisterListener(Integer antenna, RFIDTagListener _rfidListener) {
		log.debug(this.toString() + " unregistring listenners (" + _rfidListener.toString() + ") linked to those antenna: " + this.antennaSet.toString());
		for (Map.Entry<Integer, Vector<RFIDConsumer>> set : antennaConsumers.entrySet()) {
			if (set.getKey() == antenna) {
				Vector<RFIDConsumer> rfidConsumers = new Vector<RFIDConsumer>(set.getValue());
    			for (RFIDConsumer rfidConsumer : rfidConsumers) {
    				rfidConsumer.unregisterListener(_rfidListener);
    			}
			}
		}
    }

    public void printConsumersStat() {
    	log.info("Consummer tag stat report: ");
		for (Map.Entry<Integer, Integer> set : antennaSet.entrySet()) {
			Vector<RFIDConsumer> rfidConsumers = antennaConsumers.get(set.getKey());
			for (RFIDConsumer rfidConsumer : rfidConsumers)
				log.info("\t\t antenna: " + rfidConsumer.getRFIDAntenna() + 
						" total scan: " + rfidConsumer.getTag_cnt());
		}
    }
}
