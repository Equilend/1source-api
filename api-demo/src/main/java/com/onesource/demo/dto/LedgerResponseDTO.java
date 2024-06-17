package com.onesource.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * LedgerResponseDTO
 */
public class LedgerResponseDTO {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("responseDateTime")
  private LocalDateTime responseDateTime = null;

  @JsonProperty("requestId")
  private String requestId = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("resourceUri")
  private String resourceUri = null;

  public LedgerResponseDTO code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   **/
//  @Schema(description = "")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public LedgerResponseDTO type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   **/
//  @Schema(description = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public LedgerResponseDTO responseDateTime(LocalDateTime responseDateTime) {
    this.responseDateTime = responseDateTime;
    return this;
  }

  /**
   * Get responseDateTime
   * @return responseDateTime
   **/
//  @Schema(description = "")
  public LocalDateTime getResponseDateTime() {
    return responseDateTime;
  }

  public void setResponseDateTime(LocalDateTime responseDateTime) {
    this.responseDateTime = responseDateTime;
  }

  public LedgerResponseDTO requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Get requestId
   * @return requestId
   **/
//  @Schema(description = "")
  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public LedgerResponseDTO message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   **/
//  @Schema(required = true, description = "")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LedgerResponseDTO resourceUri(String resourceUri) {
    this.resourceUri = resourceUri;
    return this;
  }

  /**
   * Get resourceUri
   * @return resourceUri
   **/
//  @Schema(description = "")
  public String getResourceUri() {
    return resourceUri;
  }

  public void setResourceUri(String resourceUri) {
    this.resourceUri = resourceUri;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LedgerResponseDTO ledgerResponse = (LedgerResponseDTO) o;
    return Objects.equals(this.code, ledgerResponse.code) &&
            Objects.equals(this.type, ledgerResponse.type) &&
            Objects.equals(this.responseDateTime, ledgerResponse.responseDateTime) &&
            Objects.equals(this.requestId, ledgerResponse.requestId) &&
            Objects.equals(this.message, ledgerResponse.message) &&
            Objects.equals(this.resourceUri, ledgerResponse.resourceUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, type, responseDateTime, requestId, message, resourceUri);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LedgerResponseDTO {\n");

    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    responseDateTime: ").append(toIndentedString(responseDateTime)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    resourceUri: ").append(toIndentedString(resourceUri)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
