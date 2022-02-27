package com.wms.uhfrfid.service.rfid.reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RFIDReaderStartup {
    private static RFIDReaderStartup INSTANCE;
	private HashMap<Integer, Integer> antennaSetConsumers = new HashMap<Integer, Integer>(4);
	private Vector<Integer> antennaSet = null;
	private XSPSRFIDReader producer;
	private PoolRFIDConsumer consumers;
	private Thread producerThread;
    private final Logger log = LoggerFactory.getLogger(RFIDReaderStartup.class);

    public synchronized static RFIDReaderStartup getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new RFIDReaderStartup();
        }
        
        return INSTANCE;
    }

	public void startRFIDReader() {
		log.debug(this.toString() + " started");

		antennaSetConsumers.put(3, 1);

		antennaSet = new Vector<Integer>(antennaSetConsumers.size());
		for (Map.Entry<Integer, Integer> set : antennaSetConsumers.entrySet())
			antennaSet.add(set.getKey());		

		producer = new XSPSRFIDReader("192.168.1.116", 9090, "wms_rfid.ant.", antennaSet);
		consumers = new PoolRFIDConsumer("wms_rfid.ant.", "tcp://localhost:61616", antennaSetConsumers);

		producerThread = new Thread(producer);
		producerThread.start();

		consumers.startRFIDConsumers();
	}

	public HashMap<Integer, Integer> getAntennaSetConsumers() {
		return antennaSetConsumers;
	}

	public void setAntennaSetConsumers(HashMap<Integer, Integer> antennaSetConsumers) {
		this.antennaSetConsumers = antennaSetConsumers;
	}

	public Vector<Integer> getAntennaSet() {
		return antennaSet;
	}

	public void setAntennaSet(Vector<Integer> antennaSet) {
		this.antennaSet = antennaSet;
	}

	public XSPSRFIDReader getProducer() {
		return producer;
	}

	public void setProducer(XSPSRFIDReader producer) {
		this.producer = producer;
	}

	public PoolRFIDConsumer getConsumers() {
		return consumers;
	}

	public void setConsumers(PoolRFIDConsumer consumers) {
		this.consumers = consumers;
	}
}