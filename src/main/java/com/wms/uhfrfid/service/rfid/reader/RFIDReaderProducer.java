/**
 * 
 */
package com.wms.uhfrfid.service.rfid.reader;

import java.util.HashMap;
import java.util.Vector;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rfidread.Interface.IAsynchronousMessage;
import com.rfidread.Interface.ISearchDevice;
import com.rfidread.Models.Device_Model;
import com.rfidread.Models.GPI_Model;
import com.rfidread.Models.Tag_Model;

/**
 * @author hmr
 *
 */
public class RFIDReaderProducer implements IAsynchronousMessage, ISearchDevice {
    private transient String brokerURL = "tcp://localhost:61616";
    private static transient ConnectionFactory factory = null;
    private transient Connection connection = null;
    private transient Session session = null;
    private transient MessageProducer producer = null;
    private transient RFIDTag wmsRFIDTag = null;
    private String brokerStorePrefix = null;
    private Vector<Integer> antennaSet = null;
    private HashMap<Integer, Destination> destinations = null;
    private final Logger log = LoggerFactory.getLogger(RFIDReaderProducer.class);
    
	/**
	 * @param brokerURL
	 */
	public RFIDReaderProducer(String brokerURL, String brokerStorePrefix, Vector<Integer> antennaSet) throws Exception {
		this.brokerURL = new String(brokerURL);
		this.brokerStorePrefix = new String(brokerStorePrefix);
		this.antennaSet = antennaSet;

    	factory = new ActiveMQConnectionFactory(this.brokerURL);
    	connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(null);

        if (antennaSet == null)
        	; //TODO throw exception

        destinations = new HashMap<Integer, Destination>(antennaSet.size());
        for (Integer antenna : this.antennaSet)
        	destinations.put(antenna, session.createQueue(this.brokerStorePrefix + String.valueOf(antenna)));
        wmsRFIDTag = new RFIDTag();
        session.createObjectMessage(wmsRFIDTag);
	}

	@Override
	public void GPIControlMsg(GPI_Model arg0) {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS GPIControlMsg: " + arg0.ReaderName);		
	}

	@Override
	public void OutPutTags(Tag_Model tag) {
		wmsRFIDTag.resetRFIDTag();
		wmsRFIDTag.set_ANT_NUM(tag._ANT_NUM);
		wmsRFIDTag.set_EPC(tag._EPC);

        Message message;
		try {
			message = session.createObjectMessage(wmsRFIDTag);
			Destination destination = destinations.get(tag._ANT_NUM);
			if (destination == null)
				log.error("RFIDReaderProducer: No queue destination for the scanned tag " + tag._EPC + " from this antenna: "+ tag._ANT_NUM);
			else
				producer.send(destination, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void OutPutTagsOver() {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS OutPutTagsOver: ");		
	}

	@Override
	public void PortClosing(String arg0) {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS PortClosing: " + arg0);
	}

	@Override
	public void PortConnecting(String arg0) {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS PortConnecting: " + arg0);
	}

	@Override
	public void WriteDebugMsg(String arg0) {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS WriteDebugMsg: " + arg0);
	}

	@Override
	public void WriteLog(String arg0) {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS WriteLog: " + arg0);
	}

	@Override
	public void DebugMsg(String arg0) {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS DebugMsg: " + arg0);
	}

	@Override
	public void DeviceInfo(Device_Model arg0) {
		log.debug("Thread " + Thread.currentThread().getId() + " -> X-SPS DeviceInfo: " + arg0._ConnectMode + " | " + arg0._IP);
	}
}
