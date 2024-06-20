package com.onesource.demo.util;

import org.json.JSONObject;
import org.json.JSONArray;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonGenerator {

    public static JSONObject generateReturnProposalJson(String contractId) {
        JSONObject jsonObj = new JSONObject();

        JSONObject executionVenue = new JSONObject();
//        JSONArray localVenueFields = new JSONArray();
//        JSONObject localVenueField = new JSONObject();
//        localVenueField.put("localFieldName", "string");
//        localVenueField.put("localFieldValue", "string");
//        localVenueFields.put(localVenueField);
//        executionVenue.put("localVenueFields", localVenueFields);
        executionVenue.put("partyId", "TBORR-US");  // "partyName", "TestBorrower1"

//        executionVenue.put("transactionDatetime", LocalDate.now().format(DateTimeFormatter.ISO_INSTANT)); // "2024-05-31T13:25:52.693Z"
        executionVenue.put("type", "ONPLATFORM");
        executionVenue.put("venueName", "EXTERNAL");

        JSONArray venueParties = new JSONArray();
        JSONObject venueParty = new JSONObject();
        venueParty.put("partyRole", "BORROWER");
//        venueParty.put("venuePartyRefKey", "string");
        venueParties.put(venueParty);
        executionVenue.put("venueParties", venueParties);
//        executionVenue.put("venueRefKey", "string");
        jsonObj.put("executionVenue", executionVenue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().format(formatter);

        jsonObj.put("quantity", 10);
        jsonObj.put("returnDate", currentDate);
        jsonObj.put("returnSettlementDate", currentDate);

        JSONArray settlements = new JSONArray();
        JSONObject settlement = new JSONObject();
        settlement.put("partyRole", "BORROWER");
        settlement.put("internalAcctCd", "ABCD-DEF0");
//        settlement.put("settlementStatus", "NONE");

        JSONObject instruction = new JSONObject();
        instruction.put("settlementBic", "DTCYUS33");
//        instruction.put("cdsCustomerUnitId", "string");
//        instruction.put("custodianAcct", "string");
//        instruction.put("custodianBic", "string");
//        instruction.put("custodianName", "string");
//        instruction.put("dtcParticipantNumber", "string");
//        instruction.put("localAgentAcct", "IRVTBEBBXXX");
        instruction.put("dtcParticipantNumber", "0901");
        instruction.put("localAgentBic", "IRVTBEBBXXX");
//        instruction.put("localAgentName", "string");
        settlement.put("instruction", instruction);
        settlements.put(settlement);
        jsonObj.put("settlement", settlements);
//        jsonObj.put("settlement", settlement);

        jsonObj.put("settlementType", "DVP");
        jsonObj.put("collateralValue", 5000);

        return jsonObj;
    }

    public static JSONObject generateRecallProposalJson(String contractId) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("internalRefId", contractId);
        JSONObject json = new JSONObject();

        JSONObject executionVenue = new JSONObject();
        JSONArray localVenueFields = new JSONArray();
        JSONObject localVenueField = new JSONObject();
        localVenueField.put("localFieldName", "string");
        localVenueField.put("localFieldValue", "string");
        localVenueFields.put(localVenueField);
        executionVenue.put("localVenueFields", localVenueFields);
        executionVenue.put("partyId", "string");
        executionVenue.put("transactionDatetime", "2024-06-04T15:28:16.736Z");
        executionVenue.put("type", "ONPLATFORM");
        executionVenue.put("venueName", "string");

        JSONArray venueParties = new JSONArray();
        JSONObject venueParty = new JSONObject();
        venueParty.put("partyRole", "BORROWER");
        venueParty.put("venuePartyRefKey", "string");
        venueParties.put(venueParty);
        executionVenue.put("venueParties", venueParties);
        executionVenue.put("venueRefKey", "string");

        json.put("executionVenue", executionVenue);
        json.put("quantity", 0);
        json.put("recallDate", "2024-06-04");
        json.put("recallDueDate", "2024-06-04");

        System.out.println(json.toString(4));
        return jsonObj;
    }

    public static JSONObject generateContractProposalJson() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().format(formatter);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HHmmss");
        String ts = LocalDateTime.now().format(df);

        JSONObject jsonObj = new JSONObject();
        JSONArray settlementArray = new JSONArray();
        JSONObject settlementObj = new JSONObject();
        settlementObj.put("partyRole", "LENDER");
        settlementObj.put("settlementStatus", "SETTLED");
        settlementObj.put("internalAcctCd", "ABCD-DEF0");

        JSONObject instructionObj = new JSONObject();
        instructionObj.put("localAgentAcct", "483fad97725");
        instructionObj.put("localAgentBic", "OKVHawGcxP");
        instructionObj.put("localAgentName", "5Crooks, Bode and Koch");
        instructionObj.put("custodianAcct", "51sfsd372485");
        instructionObj.put("custodianBic", "GKsIzuAbtf");
        instructionObj.put("custodianName", "Weissnat - Marquardt");
        instructionObj.put("settlementBic", "DTCYUS33");
        instructionObj.put("dtcParticipantNumber", "0002");

        settlementObj.put("instruction", instructionObj);
        settlementArray.put(settlementObj);
        jsonObj.put("settlement", settlementArray);

        JSONObject tradeObj = new JSONObject();
        tradeObj.put("billingCurrency", "USD");
        tradeObj.put("dividendRatePct", 100);
        tradeObj.put("settlementDate", currentDate);
        tradeObj.put("settlementType", "DVP");
        tradeObj.put("termType", "OPEN");
        tradeObj.put("tradeDate", currentDate);

        JSONArray venuesArray = new JSONArray();
        JSONObject venueObj = new JSONObject();

//        JSONArray localVenueFieldsArray = new JSONArray();
//        JSONObject localVenueFieldObject = new JSONObject();
//        localVenueFieldObject.put("localFieldName", "LenderRef");
//        localVenueFieldObject.put("localFieldValue", "MyRef-lender");
//        localVenueFieldsArray.put(localVenueFieldObject);
//        venueObj.put("localVenueFields", localVenueFieldsArray);

        venueObj.put("partyId", "TLEN-US");
        venueObj.put("transactionDatetime", currentDate);
        venueObj.put("type", "ONPLATFORM");
        venueObj.put("venueName", "EXTERNAL"); // "EQL" gives "Internal Server Error"

//      "gleifLei":"213800BN4DRR1ADYGP92",
//      "legalName":"EquiLend LLC",
//      "venueName":"NGT",
//      "venueRefId":"896927053849"

        JSONArray venuePartiesArray = new JSONArray();
        JSONObject venuePartyObj = new JSONObject();
        venuePartyObj.put("partyRole", "LENDER");
        venuePartyObj.put("venuePartyId", System.getenv("USERNAME")); //  "testing!"
        venuePartiesArray.put(venuePartyObj);
        venueObj.put("venueParties", venuePartiesArray);
        venueObj.put("venueRefKey", "TEST" + ts); //  "8765449" // "GNS240530.1"
        venuesArray.put(venueObj);
        tradeObj.put("venues", venuesArray);

        JSONObject collateralObj = new JSONObject();
        collateralObj.put("collateralValue", 8933925);
        collateralObj.put("contractPrice", 8758750);
        collateralObj.put("contractValue", 8758750);
        collateralObj.put("currency", "USD");
        collateralObj.put("margin", 102);
        collateralObj.put("roundingMode", "ALWAYSUP");
        collateralObj.put("roundingRule", 10);
        collateralObj.put("type", "CASH");
        tradeObj.put("collateral", collateralObj);

        JSONObject instrumentObj = new JSONObject();
        instrumentObj.put("figi", "BBG000DMBXR2");
//        instrumentObj.put("isin", "NL0000226223");
//        instrumentObj.put("marketCd", "string");
//        instrumentObj.put("price", priceObject);
//        instrumentObj.put("sedol", "B1FSSD4");

//        JSONObject priceObject = new JSONObject();
//        priceObject.put("currency", "USD");
//        priceObject.put("unit", "SHARE");
//        priceObject.put("value", 5);
//        priceObject.put("valueDate", currentDate);
//        instrumentObj.put("price", priceObject);

        tradeObj.put("instrument", instrumentObj);
        tradeObj.put("quantity", 500);

        JSONObject rateObject = new JSONObject();
        JSONObject rebateObject = new JSONObject();
        JSONObject fixedObject = new JSONObject();
        fixedObject.put("baseRate", 5.55);
        fixedObject.put("cutoffTime", "01:00:00");
        fixedObject.put("effectiveDate", currentDate);
        fixedObject.put("effectiveRate", 5.55);
        rebateObject.put("fixed", fixedObject);
        rateObject.put("rebate", rebateObject);
        tradeObj.put("rate", rateObject);

        JSONArray transactingPartiesArray = new JSONArray();
        JSONObject lenderParty = new JSONObject();
        JSONObject lenderRef = new JSONObject();
        lenderRef.put("accountId", "5accountId55");
        lenderRef.put("brokerCd", "5brokerCd5");

        lenderRef.put("internalRefId", "GNSCTL" + ts); //"5internalRefId555"
        lenderParty.put("internalRef", lenderRef);

        JSONObject lenderInfo = new JSONObject();
        lenderInfo.put("gleifLei", "KTB500SKZSDI75VSFU40");
        lenderInfo.put("internalPartyId", "5internalPartyId55");
        lenderInfo.put("partyId", "TLEN-US");
        lenderInfo.put("partyName", "TestLender1");
        lenderParty.put("party", lenderInfo);
        lenderParty.put("partyRole", "LENDER");
        transactingPartiesArray.put(lenderParty);

        JSONObject borrowerParty = new JSONObject();
        JSONObject borrowerRef = new JSONObject();
        borrowerRef.put("accountId", "5this5");
        borrowerRef.put("brokerCd", "5this");
        borrowerParty.put("internalRef", borrowerRef);

        JSONObject borrowerInfo = new JSONObject();
        borrowerInfo.put("gleifLei", "KTB500SKZSDI75VSFU40");
        borrowerInfo.put("internalPartyId", "5this555");
        borrowerInfo.put("partyId", "TBORR-US");
        borrowerInfo.put("partyName", "TestBorrower1");
        borrowerParty.put("party", borrowerInfo);
        borrowerParty.put("partyRole", "BORROWER");
        transactingPartiesArray.put(borrowerParty);
        tradeObj.put("transactingParties", transactingPartiesArray);
        jsonObj.put("trade", tradeObj);
        return jsonObj;
    }

    public static JSONObject generateContractApprovalJson(String contractId) {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("internalRefId", contractId);
//        jsonObj.put("roundingMode", "ALWAYSUP");  -- Not allowed
//        jsonObj.put("roundingRule", 1);           -- Not allowed

        JSONObject settlementObj = new JSONObject();
        settlementObj.put("internalAcctCd", "ABCD-DEF0");
        settlementObj.put("partyRole", "BORROWER");
        settlementObj.put("settlementStatus", "NONE");

        JSONObject instructionObj = new JSONObject();
//        instructionObj.put("localAgentAcct", "25451996");
//        instructionObj.put("localAgentBic", "SUZEEAR1");
//        instructionObj.put("localAgentName", "Kautzer, Bergnaum and Gulgowski");
        instructionObj.put("localAgentAcct", "483fad97725");
        instructionObj.put("localAgentBic", "OKVHawGcxP");
        instructionObj.put("localAgentName", "5Crooks, Bode and Koch");
        instructionObj.put("custodianAcct", "51sfsd372485");
        instructionObj.put("custodianBic", "GKsIzuAbtf");
        instructionObj.put("custodianName", "Weissnat - Marquardt");
        instructionObj.put("settlementBic", "DTCYUS33");
        instructionObj.put("dtcParticipantNumber", "0002");
//        instructionObj.put("cdsCustomerUnitId", "string");

        settlementObj.put("instruction", instructionObj);
        jsonObj.put("settlement", settlementObj);
        return jsonObj;
    }

    public static JSONObject generateSettlementStatusUpdateJson(String status) {
        JSONObject settlement = new JSONObject();
        settlement.put("settlementStatus", status);
        return settlement;
    }
}
