package com.os.marktomarket;

import java.io.Serializable;

public class LoanMark implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1130479664165725113L;
	
	String loanId;
	String currentMarkEventId = null;
	Double currentMark = 0d;
	String lastMarkEventId = null;
	Double lastMark = 0d;

	public LoanMark(String loanId) {
		super();
		this.loanId = loanId;
	}

	public String getLoanId() {
		return loanId;
	}

	public String getCurrentMarkEventId() {
		return currentMarkEventId;
	}

	public void setCurrentMarkEventId(String currentMarkEventId) {
		this.currentMarkEventId = currentMarkEventId;
	}

	public Double getCurrentMark() {
		return currentMark;
	}

	public void setCurrentMark(Double currentMark) {
		this.currentMark = currentMark;
	}

	public String getLastMarkEventId() {
		return lastMarkEventId;
	}

	public void setLastMarkEventId(String lastMarkEventId) {
		this.lastMarkEventId = lastMarkEventId;
	}

	public Double getLastMark() {
		return lastMark;
	}

	public void setLastMark(Double lastMark) {
		this.lastMark = lastMark;
	}

}
