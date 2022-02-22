/**
 * 
 */
package com.wms.uhfrfid.service.rfid.reader;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author hmr
 *
 */
public class RFIDTagListener { //implements Callable<Integer> {

	protected PoolRFIDConsumer consumers = null;
	protected String job = null;
	protected Vector<Integer> antennaSet = null;
	protected HashMap<String, RFIDTag> rfidTagSet = null;
	protected int rfidTagCount = 0;
	protected int rfidTagToRead = 0;
	protected int timeout = 0;
	
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

		for (Integer antenna : this.antennaSet)
			this.consumers.registerListener(antenna, this);
	}

	public synchronized void onMessage(RFIDTag rfidTag) {
		try {
			//do something here
			System.out.println("RFIDTagListener : " + job + " Antenna: " + rfidTag.get_ANT_NUM() + " # " + rfidTag.get_EPC());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Vector<RFIDTag> call(int timeout) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

/*
	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		return 555;
	}
*/

/*
	@Override
	public synchronized void run() {
		while (true) {
			try {
				wait();
				System.out.println("@@@@@@@@@@@@@@@@@@@@@ ######################### Antenna: ");
				//			Thread.sleep(10000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
*/
}
