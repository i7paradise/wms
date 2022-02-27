package com.wms.uhfrfid.service.rfid.reader;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class RFIDConsumer implements Runnable {

    private static transient ConnectionFactory factory = null;
    private transient Connection connection = null;
    private transient Session session = null;
    private MessageConsumer consumer = null;
    private Destination RFIDqueue = null;
    private Vector<RFIDTagListener> rfidListeners = null;
    private int rfidAntenna;
    private String brokerUrl = null;
    private String brokerStore = null;
    private int tag_cnt = 0;

    
    public RFIDConsumer(String brokerStorePrefix, String brokerURL, int rfidAntenna) throws JMSException {
    	this.brokerUrl = new String(brokerURL);
    	this.rfidAntenna = rfidAntenna;
    	this.brokerStore = new String(brokerStorePrefix) + String.valueOf(this.rfidAntenna);

    	factory = new ActiveMQConnectionFactory(this.brokerUrl);
//    	factory.setTrustAllPackages(true);
    	connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        RFIDqueue = session.createQueue(this.brokerStore);
        consumer = session.createConsumer(RFIDqueue);
        rfidListeners = new Vector<RFIDTagListener>();
    }
    
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    public void run() {
    	try {
            for (; ; ) {
            	Message message = consumer.receive();

            	tag_cnt++;
            	if (message instanceof ObjectMessage) {
            		ObjectMessage objMessage = (ObjectMessage) message;
            		RFIDTag wmsTag = (RFIDTag) objMessage.getObject();
                    
            		for (RFIDTagListener rfidListener : rfidListeners) {
//            			wmsTag.set_EPC(this.toString());
                       if (rfidAntenna == wmsTag.get_ANT_NUM())
                    	   rfidListener.onMessage(wmsTag);
                       else
                    	   System.out.println("RFID Consumer: wrong antenna: " + rfidAntenna + " ### " + wmsTag.get_ANT_NUM());
                   }
            	} else {
            		System.out.println("RFID Consumer: received message is broken");            		
            	}
            }
    	} catch(Exception e) {
    		System.out.println("Consumer @ exeception: " + e.getMessage());
    	}
    }

    public void registerListener(RFIDTagListener _rfidListener) {
    	rfidListeners.add(_rfidListener);
    }

    public void unregisterListener(RFIDTagListener _rfidListener) {
    	for (RFIDTagListener rfidListener : rfidListeners)
    	if (rfidListener == _rfidListener)
    		rfidListeners.remove(_rfidListener);
    }

	public Session getSession() {
		return session;
	}

	public int getTag_cnt() {
		return tag_cnt;
	}

	public void setTag_cnt(int tag_cnt) {
		this.tag_cnt = tag_cnt;
	}

	public int getRFIDAntenna() {
		return rfidAntenna;
	}

	public void setRFIDAntenna(int rfidAntenna) {
		this.rfidAntenna = rfidAntenna;
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getBrokerStore() {
		return brokerStore;
	}

	public void setBrokerStore(String brokerStore) {
		this.brokerStore = brokerStore;
	}

	
}
