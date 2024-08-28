package com.onesource.demo.util;

import org.json.JSONObject;
import org.json.JSONArray;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.onesource.demo.ApiDemoApp.PV_EQLRET;

public class JsonGenerator {

    public static JSONObject generateReturnProposalJson(String submitter, String contractId) {
        JSONObject jsonObj = new JSONObject();

        // Added som of these to fix error "If venue type is ONPLATFORM, partyId, gleifLei and venueRefKey are mandatory"
        // but they are NOT in the schema
        JSONObject executionVenue = new JSONObject();
//        executionVenue.put("type", "OFFPLATFORM");
//        executionVenue.put("type", "ONPLATFORM");
//        executionVenue.put("partyId", "TBORR-US");  // "partyName", "TestBorrower1"
//        executionVenue.put("gleifLei", "KTB500SKZSDI75VSFU40");
        executionVenue.put("venueRefKey", "100047251111");  // Must match the contract venueRefKey ?
        executionVenue.put("venueName", "R2S");

        JSONObject venueParty = new JSONObject();

        if (submitter.equals(PV_EQLRET)) {
            executionVenue.put("type", "ONPLATFORM");
            venueParty.put("partyId", "EQLRET");  // Submitting from PV
            venueParty.put("gleifLei", "213800BN4DRR1ADYGP92");
        } else {
            executionVenue.put("type", "OFFPLATFORM");
            venueParty.put("partyId", "TBORR-US");  // Use these if submitting as Borrower
            venueParty.put("gleifLei", "KTB500SKZSDI75VSFU40");
        }

        venueParty.put("internalPartyId", "LE1201");
//        venueParty.put("partyRole", "BORROWER");
//        venueParty.put("venuePartyRefKey", "string");

//        StringBuffer venuePartyRefKey = new StringBuffer();
//        venuePartyRefKey.append(JsonUtils.getJsonString(kafkaJson, "borrowerOrgId"));
//        venuePartyRefKey.append("-").append(JsonUtils.getJsonString(kafkaJson, "borrowerLEId"));
//        venueParty.put("venuePartyRefKey", venuePartyRefKey.toString());

//        venueParties.put(venueParty);
//        executionVenue.put("venueParties", venueParty);
        executionVenue.put("party", venueParty);
        jsonObj.put("executionVenue", executionVenue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().format(formatter);

        jsonObj.put("quantity", 10);
        jsonObj.put("returnDate", currentDate);
        jsonObj.put("returnSettlementDate", currentDate);
        jsonObj.put("settlementType", "DVP");
        jsonObj.put("collateralValue", 5000);

        JSONArray settlements = new JSONArray();
        JSONObject settlement = new JSONObject();
        settlement.put("partyRole", "BORROWER");
        settlement.put("internalAcctCd", "ABCD-DEF0");   // Required
//        settlement.put("settlementStatus", "NONE");

        JSONObject instruction = new JSONObject();
        instruction.put("settlementBic", "DTCYUS33");   // Required
        instruction.put("dtcParticipantNumber", "0901");
        settlement.put("instruction", instruction);
//        instruction.put("cdsCustomerUnitId", "string");
//        instruction.put("custodianAcct", "string");
//        instruction.put("custodianBic", "string");
//        instruction.put("custodianName", "string");
//        instruction.put("localAgentAcct", "IRVTBEBBXXX");
//        instruction.put("localAgentBic", "IRVTBEBBXXX");
//        instruction.put("localAgentName", "string");
        settlements.put(settlement);
        jsonObj.put("settlement", settlements);
        return jsonObj;
    }

    public static JSONObject generateRecallProposalJson(String contractId) {
        JSONObject json = new JSONObject();
        JSONObject executionVenue = new JSONObject();
//        JSONArray localVenueFields = new JSONArray();
//        JSONObject localVenueField = new JSONObject();
//        localVenueField.put("localFieldName", "string");
//        localVenueField.put("localFieldValue", "string");
//        localVenueFields.put(localVenueField);
//        executionVenue.put("localVenueFields", localVenueFields);
        executionVenue.put("partyId", "TLEND-US");  // "partyName", "TestBorrower1"
//        executionVenue.put("transactionDatetime", "2024-06-25T15:28:16.736Z");
        executionVenue.put("type", "OFFPLATFORM");
        executionVenue.put("venueName", "EXTERNAL");
        executionVenue.put("venueRefKey", "100047250692");  // equilendTxnId for an open contract in PTS

        JSONArray venueParties = new JSONArray();
        JSONObject venueParty = new JSONObject();
        venueParty.put("partyRole", "LENDER");
        venueParty.put("venuePartyRefKey", "1202");
        venueParties.put(venueParty);
        executionVenue.put("venueParties", venueParties);

        json.put("executionVenue", executionVenue);
        json.put("quantity", 5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().format(formatter);

        json.put("recallDate", currentDate);
        json.put("recallDueDate", currentDate);

        System.out.println(json.toString(4));
        return json;
    }

    public static JSONObject generateContractProposalJson() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().plusDays(1) .format(formatter);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HHmmss");
        String ts = LocalDateTime.now().format(df);

        JSONObject jsonObj = new JSONObject();
        JSONObject tradeObj = new JSONObject();
        JSONObject venueParty = new JSONObject();
        venueParty.put("partyId", "EQLNGT");
//        venueParty.put("partyName", "TestLender1");
        venueParty.put("gleifLei", "213800BN4DRR1ADYGP92");
        venueParty.put("internalPartyId", "LE1202");

        JSONObject venueObj = new JSONObject();
        venueObj.put("type", "ONPLATFORM");
        venueObj.put("partyId", "EQLNGT");
        venueObj.put("venueRefKey", "100047251111");
        venueObj.put("transactionDatetime", currentDate);
        venueObj.put("party", venueParty);

        // If venue type is ONPLATFORM, partyId, gleifLei and venueRefKey are mandatory
//        venueObj.put("partyId", "TLEN-US");
//        venueObj.put("venueName", "R2S"); // Was EXTERNAL; "EQL" gives "Internal Server Error"

//        JSONObject venuePartyObj = new JSONObject();
//        venuePartyObj.put("partyRole", "LENDER");
//        venuePartyObj.put("venuePartyId", System.getenv("USERNAME")); //  "testing!"

//        JSONArray venuePartiesArray = new JSONArray();
//        venuePartiesArray.put(venuePartyObj);
//        venueObj.put("venueParties", venuePartiesArray);

        JSONArray venuesArray = new JSONArray();
        venuesArray.put(venueObj);
        tradeObj.put("venues", venuesArray);

        JSONObject instrumentObj = new JSONObject();
        instrumentObj.put("figi", "BBG000DMBXR2");  // IBM: BBG000BLNNH6
//        instrumentObj.put("isin", "NL0000226223");
//        instrumentObj.put("marketCd", "string");
//        instrumentObj.put("price", priceObject);
//        instrumentObj.put("sedol", "B1FSSD4");
        tradeObj.put("instrument", instrumentObj);

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

        tradeObj.put("quantity", 500);
        tradeObj.put("billingCurrency", "USD");
        tradeObj.put("dividendRatePct", 100);
        tradeObj.put("settlementDate", currentDate);
        tradeObj.put("settlementType", "DVP");
        tradeObj.put("termType", "OPEN");
        tradeObj.put("tradeDate", currentDate);

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

//        JSONObject priceObject = new JSONObject();
//        priceObject.put("currency", "USD");
//        priceObject.put("unit", "SHARE");
//        priceObject.put("value", 5);
//        priceObject.put("valueDate", currentDate);
//        instrumentObj.put("price", priceObject);

        JSONArray transactingPartiesArray = new JSONArray();
        JSONObject lenderTransParty = new JSONObject();
        lenderTransParty.put("partyRole", "LENDER");

        JSONObject lenderParty = new JSONObject();
        lenderParty.put("partyId", "TLEN-US");
        lenderParty.put("partyName", "TestLender1");
        lenderParty.put("gleifLei", "KTB500SKZSDI75VSFU40");
        lenderParty.put("internalPartyId", "LE1202");   // 5internalPartyId55
        lenderTransParty.put("party", lenderParty);
        transactingPartiesArray.put(lenderTransParty);

        JSONObject lenderInternalRef = new JSONObject();
        lenderInternalRef.put("accountId", "5accountId55");
        lenderInternalRef.put("brokerCd", "5brokerCd5");
        lenderInternalRef.put("internalRefId", "GNSCTL" + ts); //"5internalRefId555"
        lenderTransParty.put("internalRef", lenderInternalRef);

        JSONObject borrowerTransParty = new JSONObject();
        borrowerTransParty.put("partyRole", "BORROWER");

        JSONObject borrowerParty = new JSONObject();
        borrowerParty.put("partyId", "TBORR-US");
        borrowerParty.put("partyName", "TestBorrower1");
        borrowerParty.put("gleifLei", "KTB500SKZSDI75VSFU40");
        borrowerParty.put("internalPartyId", "LE1201");  // 5this555
        borrowerTransParty.put("party", borrowerParty);
        transactingPartiesArray.put(borrowerTransParty);

        JSONObject borrowerInternalRef = new JSONObject();
        borrowerInternalRef.put("accountId", "5this5");
        borrowerInternalRef.put("brokerCd", "5this");
        lenderInternalRef.put("internalRefId", "GNSCTB" + ts); //"5internalRefId555"
        borrowerTransParty.put("internalRef", borrowerInternalRef);
        tradeObj.put("transactingParties", transactingPartiesArray);
        jsonObj.put("trade", tradeObj);

        JSONObject settlementObj = new JSONObject();
        settlementObj.put("partyRole", "LENDER");
        settlementObj.put("settlementStatus", "NONE");
        settlementObj.put("internalAcctCd", "ABCD-DEF0");

        JSONObject instructionObj = new JSONObject();
        instructionObj.put("settlementBic", "DTCYUS33");
        instructionObj.put("dtcParticipantNumber", "0002");
        instructionObj.put("localAgentAcct", "483fad97725");
        instructionObj.put("localAgentBic", "OKVHawGcxP");
        instructionObj.put("localAgentName", "5Crooks, Bode and Koch");
        instructionObj.put("custodianAcct", "51sfsd372485");
        instructionObj.put("custodianBic", "GKsIzuAbtf");
        instructionObj.put("custodianName", "Weissnat - Marquardt");
        settlementObj.put("instruction", instructionObj);
        JSONArray settlementArray = new JSONArray();
        settlementArray.put(settlementObj);
        jsonObj.put("settlement", settlementArray);
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

    public static JSONObject generateRerateProposalJson(String contractId) {
        JSONObject jsonObj = new JSONObject();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();  // Should be TODAY or it won't get applied until effectiveDate
        String currentDate = now.format(formatter);

//        now = now.plusDays(1);
        String effectiveDate = now.format(formatter);

        JSONObject executionVenue = new JSONObject();
        executionVenue.put("type", "ONPLATFORM");
        executionVenue.put("venueRefKey", "myRerateId-123");

//        DateTimeFormatter df = DateTimeFormatter.ofPattern("HHmmss");
//        String ts = LocalDateTime.now().format(df);

        JSONObject venueParty = new JSONObject();
        venueParty.put("partyId", "EQLRER");
//        venueParty.put("partyName", "TestLender1");
        venueParty.put("gleifLei", "213800BN4DRR1ADYGP92");
        venueParty.put("internalPartyId", "LE1202");
        executionVenue.put("party", venueParty);
//        venueObj.put("transactionDatetime", currentDate + "T" + ts + ".000Z");
        jsonObj.put("executionVenue", executionVenue);

        JSONObject rateObj = new JSONObject();
        JSONObject rabate = new JSONObject();
        JSONObject fixed = new JSONObject();
        fixed.put("baseRate", -.25);
        fixed.put("effectiveRate", -.25);
        fixed.put("effectiveDate", effectiveDate);
        fixed.put("cutoffTime", "11:00");
        rabate.put("fixed", fixed);
        rateObj.put("rebate", rabate);
        jsonObj.put("rate", rateObj);
        return jsonObj;
    }

    public static JSONObject generateSettlementStatusUpdateJson(String status) {
        JSONObject settlement = new JSONObject();
        settlement.put("settlementStatus", status);
        return settlement;
    }

    public static JSONObject generateReturnAckJson(JSONObject returnObj, String ackType) {
        JSONObject apiJson = new JSONObject();
        apiJson.put("acknowledgementType", ackType);  // POSITIVE or NEGATIVE

        JSONObject settlement = new JSONObject();
        settlement.put("partyRole", "LENDER");

        JSONArray settlementArray = returnObj.getJSONArray("settlement");
        JSONObject instruction = new JSONObject();

        for (int i = 0; i < settlementArray.length(); i++) {
            JSONObject returnSettlement = settlementArray.getJSONObject(i);
            String partyRole = returnSettlement.getString("partyRole");

            if ("LENDER".equals(partyRole)) {
                putIfNotNull(settlement, "internalAcctCd", returnSettlement.optString("internalAcctCd"));
                JSONObject returnInstruction = returnSettlement.getJSONObject("instruction");

                // Maybe attach entire returnInstruction to settlement ?
                putIfNotNull(instruction, "settlementBic", returnInstruction.optString("settlementBic"));
                putIfNotNull(instruction, "dtcParticipantNumber", returnInstruction.optString("dtcParticipantNumber"));
            }
        }

        if (instruction.length() > 0) {
            settlement.put("instruction", instruction);
            apiJson.put("settlement", settlement);
        }
        else {
            System.out.println("No Borrower settlement instruction found in returnObj!");
        }
        return apiJson;
    }

    public static void putIfNotNull(JSONObject jsonObj, String key, Object value) {
        if (value != null) {
            jsonObj.put(key, value);
        }
        else {
            System.out.println("Missing " + key + ", value not found");
        }
    }
}
