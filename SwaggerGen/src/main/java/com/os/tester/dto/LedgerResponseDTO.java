package com.onesource.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * LedgerResponseDTO
 */
public class LedgerResponseDTO {
  @JsonProperty("timestamp")
  private LocalDateTime timestamp = null;

  @JsonProperty("status")
  private Integer status = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("path")
  private String path = null;

  public LedgerResponseDTO timestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public LedgerResponseDTO status(Integer status) {
    this.status = status;
    return this;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public LedgerResponseDTO message(String message) {
    this.message = message;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LedgerResponseDTO path(String path) {
    this.path = path;
    return this;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
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
    return Objects.equals(this.timestamp, ledgerResponse.timestamp) &&
        Objects.equals(this.status, ledgerResponse.status) &&
        Objects.equals(this.message, ledgerResponse.message) &&
        Objects.equals(this.path, ledgerResponse.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, status, message, path);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LedgerResponseDTO {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
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
