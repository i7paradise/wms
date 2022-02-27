/**
 * 
 */
package com.wms.uhfrfid.service.rfid.reader;

import java.util.Vector;

import com.rfidread.RFIDReader;
import com.rfidread.Tag6C;
import com.rfidread.Enumeration.eAntennaNo;
import com.rfidread.Enumeration.eReadType;
import com.rfidread.Models.Tag_Model;

/**
 * @author hmr
 *
 */
public class XSPSRFIDReader implements Runnable {

	private String readerIP;
	private int readerPort;
	private String ConnId;
	private RFIDReaderProducer rfidReaderProducer = null;
	private transient String brokerURL = "tcp://localhost:61616";
	private String brokerStorePrefix = null;
	private Vector<Integer> antennaSet = null;

	/**
	 * @param readerIP
	 * @param readerPort
	 */
	public XSPSRFIDReader(String readerIP, int readerPort, String brokerStorePrefix,
			Vector<Integer> antennaSet) {
		this.readerIP = readerIP;
		this.readerPort = readerPort;
		this.ConnId = readerIP + ":" + String.valueOf(this.readerPort);
		this.antennaSet = antennaSet;
		this.brokerStorePrefix = new String(brokerStorePrefix);
	}

	
	public String getReaderIP() {
		return readerIP;
	}


	public void setReaderIP(String readerIP) {
		this.readerIP = readerIP;
	}


	public int getReaderPort() {
		return readerPort;
	}


	public void setReaderPort(int readerPort) {
		this.readerPort = readerPort;
	}


	public String getBrokerURL() {
		return brokerURL;
	}


	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}


	boolean connectRFIDReader() {
		try {
			rfidReaderProducer = new RFIDReaderProducer(brokerURL, brokerStorePrefix, antennaSet);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		if (RFIDReader.CreateTcpConn(ConnId, rfidReaderProducer)) {
			System.out.println("Connect success!\n");
			try {
				System.out.println("Thread " + Thread.currentThread().getId() + " Config: " + RFIDReader._Config.GetEPCBaseBandParam(ConnId));
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			RFIDReader.Stop(ConnId);
		} else {
			System.out.println("Connect failure!\n");
			return false;
		}
		return true;
	}

	void stopRFIDReader() {
		RFIDReader.Stop(ConnId);
	}

	void closeRFIDReader() {
		RFIDReader.CloseConn(ConnId);
	}

	@Override
	public void run() {
		int ret;

		if (connectRFIDReader() == false)
			return;

//		int antennaSet = eAntennaNo._1.GetNum() | eAntennaNo._2.GetNum() | eAntennaNo._3.GetNum() | eAntennaNo._4.GetNum();
		int antennaSet = eAntennaNo._3.GetNum();
		ret = Tag6C.GetEPC(ConnId, antennaSet, eReadType.Inventory);
		if (ret == 0) {
			System.out.println("XSPSRFIDReader Success");
		} else {
			System.out.println("XSPSRFIDReader Failed");
		}

		while (true)
			;
	}

}
