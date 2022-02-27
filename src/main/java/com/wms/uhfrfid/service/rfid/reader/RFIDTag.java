/**
 * 
 */
package com.wms.uhfrfid.service.rfid.reader;

import java.io.Serializable;

/**
 * @author hmr
 *
 */
public class RFIDTag implements Serializable {

	private String _ReaderName = "";
	private String _PC = null;
	private String _EPC = null;
	private String _TagType = "6C";
	private int _ANT_NUM = 0;
	private int readCount = 0;

	public String get_ReaderName() {
		return _ReaderName;
	}

	public void set_ReaderName(String _ReaderName) {
		this._ReaderName = _ReaderName;
	}

	public String get_PC() {
		return _PC;
	}

	public void set_PC(String _PC) {
		this._PC = _PC;
	}

	public String get_EPC() {
		return _EPC;
	}

	public void set_EPC(String _EPC) {
		this._EPC = _EPC;
	}

	public String get_TagType() {
		return _TagType;
	}

	public void set_TagType(String _TagType) {
		this._TagType = _TagType;
	}

	public int get_ANT_NUM() {
		return _ANT_NUM;
	}

	public void set_ANT_NUM(int _ANT_NUM) {
		this._ANT_NUM = _ANT_NUM;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public void incReadCount() {
		this.readCount++;
	}

	public void resetRFIDTag() {
		set_ReaderName(null);
		set_EPC(null);
		set_PC(null);
		set_ReaderName(null);
		set_TagType(null);
	}

}
