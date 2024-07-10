package com.os.console.api;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import com.os.client.model.LedgerResponse;

public class LedgerException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3924152356771566492L;

	@SerializedName("code")
	private String code = null;

	@SerializedName("type")
	private String type = null;

	@SerializedName("responseDateTime")
	private OffsetDateTime responseDateTime = null;

	@SerializedName("message")
	private String message = null;

	@SerializedName("resourceUri")
	private String resourceUri = null;

	public LedgerException code(String code) {
		this.code = code;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LedgerException type(String type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LedgerException responseDateTime(OffsetDateTime responseDateTime) {
		this.responseDateTime = responseDateTime;
		return this;
	}

	public OffsetDateTime getResponseDateTime() {
		return responseDateTime;
	}

	public void setResponseDateTime(OffsetDateTime responseDateTime) {
		this.responseDateTime = responseDateTime;
	}

	public LedgerException message(String message) {
		this.message = message;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LedgerException resourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
		return this;
	}

	public String getResourceUri() {
		return resourceUri;
	}

	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LedgerException LedgerException = (LedgerException) o;
		return Objects.equals(this.code, LedgerException.code) && Objects.equals(this.type, LedgerException.type)
				&& Objects.equals(this.responseDateTime, LedgerException.responseDateTime)
				&& Objects.equals(this.message, LedgerException.message)
				&& Objects.equals(this.resourceUri, LedgerException.resourceUri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, type, responseDateTime, message, resourceUri);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class LedgerException {\n");

		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    responseDateTime: ").append(toIndentedString(responseDateTime)).append("\n");
		sb.append("    message: ").append(toIndentedString(message)).append("\n");
		sb.append("    resourceUri: ").append(toIndentedString(resourceUri)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
	
	public LedgerResponse getLedgerResponse() {
		
		LedgerResponse response = new LedgerResponse();
		response.setCode(code);
		response.setType(type);
		response.setResponseDateTime(responseDateTime);
		response.setMessage(message);
		response.setResourceUri(resourceUri);
		
		return response;
	}

}
