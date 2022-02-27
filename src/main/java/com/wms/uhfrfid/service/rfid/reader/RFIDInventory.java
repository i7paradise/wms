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
//public class RFIDInventory implements Runnable {
public class RFIDInventory extends RFIDTagListener implements Callable<HashMap<String, RFIDTag>> {	

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
			//do something here
//			System.out.println(job + " Antenna: " + rfidTag.get_ANT_NUM() + " # " + rfidTag.get_EPC());
			if (rfidTagCount < rfidTagToRead) {
				RFIDTag rfidTagLookup = rfidTagSet.get(rfidTag.get_EPC());
				if (rfidTagLookup == null) {
					rfidTag.setReadCount(0);
					rfidTagSet.put(rfidTag.get_EPC(), rfidTag);
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
		// TODO Auto-generated method stub
		int cnt = 0;

		while (true) {
			try {
				wait(timeout);
				System.out.println("@@@@@@@@@@@@@@@@@@@@@ ######################### Antenna: " + cnt);
				return rfidTagSet;
				//			Thread.sleep(10000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

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
