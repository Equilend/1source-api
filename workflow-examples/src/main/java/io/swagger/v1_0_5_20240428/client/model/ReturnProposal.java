/*
 * 1Source Ledger API
 * 1Source Ledger API provides client access to the 1Source Ledger. You can find out more about 1Source at [https://equilend.com](https://equilend.com).  This specification is work in progress. The design is meant to model the securities lending life cycle in as clean a way as possible while being robust enough to easily translate to ISLA CDM workflows and data model.  API specification is the intellectual property of EquiLend LLC and should not be copied or disseminated in any way. 
 *
 * OpenAPI spec version: 1.0.4
 * Contact: 1source_help@equilend.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.v1_0_5_20240428.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.swagger.v1_0_5_20240428.client.model.PartySettlementInstruction;
import io.swagger.v1_0_5_20240428.client.model.SettlementType;
import io.swagger.v1_0_5_20240428.client.model.Venue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.time.LocalDate;
/**
 * ReturnProposal
 */

@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-04-10T20:43:23.154280082Z[GMT]")

public class ReturnProposal {
  @SerializedName("executionVenue")
  private Venue executionVenue = null;

  @SerializedName("quantity")
  private Integer quantity = null;

  @SerializedName("returnDate")
  private LocalDate returnDate = null;

  @SerializedName("returnSettlementDate")
  private LocalDate returnSettlementDate = null;

  @SerializedName("collateralValue")
  private Double collateralValue = null;

  @SerializedName("settlementType")
  private SettlementType settlementType = null;

  @SerializedName("settlement")
  private PartySettlementInstruction settlement = null;

  public ReturnProposal executionVenue(Venue executionVenue) {
    this.executionVenue = executionVenue;
    return this;
  }

   /**
   * Get executionVenue
   * @return executionVenue
  **/
  @Schema(description = "")
  public Venue getExecutionVenue() {
    return executionVenue;
  }

  public void setExecutionVenue(Venue executionVenue) {
    this.executionVenue = executionVenue;
  }

  public ReturnProposal quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Get quantity
   * @return quantity
  **/
  @Schema(required = true, description = "")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ReturnProposal returnDate(LocalDate returnDate) {
    this.returnDate = returnDate;
    return this;
  }

   /**
   * Get returnDate
   * @return returnDate
  **/
  @Schema(required = true, description = "")
  public LocalDate getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(LocalDate returnDate) {
    this.returnDate = returnDate;
  }

  public ReturnProposal returnSettlementDate(LocalDate returnSettlementDate) {
    this.returnSettlementDate = returnSettlementDate;
    return this;
  }

   /**
   * Get returnSettlementDate
   * @return returnSettlementDate
  **/
  @Schema(required = true, description = "")
  public LocalDate getReturnSettlementDate() {
    return returnSettlementDate;
  }

  public void setReturnSettlementDate(LocalDate returnSettlementDate) {
    this.returnSettlementDate = returnSettlementDate;
  }

  public ReturnProposal collateralValue(Double collateralValue) {
    this.collateralValue = collateralValue;
    return this;
  }

   /**
   * Get collateralValue
   * @return collateralValue
  **/
  @Schema(description = "")
  public Double getCollateralValue() {
    return collateralValue;
  }

  public void setCollateralValue(Double collateralValue) {
    this.collateralValue = collateralValue;
  }

  public ReturnProposal settlementType(SettlementType settlementType) {
    this.settlementType = settlementType;
    return this;
  }

   /**
   * Get settlementType
   * @return settlementType
  **/
  @Schema(description = "")
  public SettlementType getSettlementType() {
    return settlementType;
  }

  public void setSettlementType(SettlementType settlementType) {
    this.settlementType = settlementType;
  }

  public ReturnProposal settlement(PartySettlementInstruction settlement) {
    this.settlement = settlement;
    return this;
  }

   /**
   * Get settlement
   * @return settlement
  **/
  @Schema(description = "")
  public PartySettlementInstruction getSettlement() {
    return settlement;
  }

  public void setSettlement(PartySettlementInstruction settlement) {
    this.settlement = settlement;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReturnProposal returnProposal = (ReturnProposal) o;
    return Objects.equals(this.executionVenue, returnProposal.executionVenue) &&
        Objects.equals(this.quantity, returnProposal.quantity) &&
        Objects.equals(this.returnDate, returnProposal.returnDate) &&
        Objects.equals(this.returnSettlementDate, returnProposal.returnSettlementDate) &&
        Objects.equals(this.collateralValue, returnProposal.collateralValue) &&
        Objects.equals(this.settlementType, returnProposal.settlementType) &&
        Objects.equals(this.settlement, returnProposal.settlement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executionVenue, quantity, returnDate, returnSettlementDate, collateralValue, settlementType, settlement);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReturnProposal {\n");
    
    sb.append("    executionVenue: ").append(toIndentedString(executionVenue)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    returnDate: ").append(toIndentedString(returnDate)).append("\n");
    sb.append("    returnSettlementDate: ").append(toIndentedString(returnSettlementDate)).append("\n");
    sb.append("    collateralValue: ").append(toIndentedString(collateralValue)).append("\n");
    sb.append("    settlementType: ").append(toIndentedString(settlementType)).append("\n");
    sb.append("    settlement: ").append(toIndentedString(settlement)).append("\n");
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

}