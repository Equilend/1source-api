package com.os.replay.model;

import java.util.List;

public class OpenFigiV3ResponseData {

	List<OpenFigiV3ResponseDataItem> data;
	String warning;
	String error;

	public List<OpenFigiV3ResponseDataItem> getData() {
		return data;
	}

	public void setData(List<OpenFigiV3ResponseDataItem> data) {
		this.data = data;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
