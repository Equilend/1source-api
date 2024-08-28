package com.onesource.demo;

import com.onesource.demo.dto.LedgerResponseDTO;
import com.onesource.demo.util.ApiHelper;
import com.onesource.demo.util.Delegation;
import com.onesource.demo.util.JsonGenerator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a simplified testing tool for querying and posting date to 1Source using the 1Source API.
 * It is up-to-date with API version 1.1.0 as of Aug 26, 2024.
 * It does not run as a Spring application, but does use various Spring classes to make the API calls.
  */
public class ApiDemoApp {

    public static final String PV_EQLRET = "PV_EQLRET";
    public static final String PV_EQLREC = "PV_EQLREC";
    public static final String BORROWER = "Borrower";
    public static final String LENDER = "Lender";

    private static ApiHelper apiHelper = new ApiHelper();

    public static Map<String, String> getTokenMap() {
        return tokenMap;
    }

    private static Map<String, String> tokenMap;

    public static void main(String[] args) {
        String pvReturnsToken = apiHelper.getAuthToken("EquilendR2SReturnsUser", "hbm57U37SXDs5g4L"); // EQLRET
        String pvRecallsToken = apiHelper.getAuthToken("EquilendR2SRecallsUser", "1maCsNUWB0sIA66G"); // EQLREC
        String pvReratesToken = apiHelper.getAuthToken("EquilendR2SReratesUser", "1maCsNUWB0sIA66G"); // EQLRER
        String borrowerToken  = apiHelper.getAuthToken("TestBorrower1User", "FqnNQyUwaenQ8K3h");
        String lenderToken    = apiHelper.getAuthToken("TestLender1User", "fjmxVeKzpzUDg3YJ");
//        String borrowerToken  = apiHelper.getAuthToken("QABorrower1User", "FqnNQyUwaenQ8K3h");
//        String lenderToken    = apiHelper.getAuthToken("QALender1User", "fjmxVeKzpzUDg3YJ");

        tokenMap = Map.of(PV_EQLRET, pvReturnsToken, PV_EQLREC, pvRecallsToken, BORROWER, borrowerToken, LENDER, lenderToken);

//        String sinceTime = "2024-06-05T01:00:00.000Z";
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(8);
        String sinceTime = localDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println("sinceTime " + sinceTime);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("since", sinceTime);
//        queryParams.add("before", "2024-08-06T05:27:26.445Z");
//        queryParams.add("fromEventId", "578588000001687606");
        queryParams.add("size", "20");

//        pvReturnsToken = apiHelper.refreshAuthToken();  // Test token refresh...

//        apiHelper.getEntity(pvReturnsToken, "parties");

//        apiHelper.getEntity(pvReturnsToken, "delegations");
//        apiHelper.getEntity(borrowerToken, "delegations");

        apiHelper.getEntity(lenderToken, "events", queryParams);  // Borrower/lender events include settlement events
//        apiHelper.getEntity(pvReturnsToken, "events", queryParams);

//        apiHelper.getEntity(pvReturnsToken, "loans", queryParams); // For PV, only OPEN contracts are retrieved
//        apiHelper.getEntity(lenderToken, "loans", queryParams);
//        apiHelper.getEntity(lenderToken, "agreements");

//        apiHelper.getEntity(pvReturnsToken, "rerates", queryParams);
//        apiHelper.getEntity(lenderToken, "rerates", queryParams);

//        apiHelper.getEntity(lenderToken, "recalls", queryParams);

        // Create a new contract, approve it, and update the status to SETTLED
//        String loanId = createOpenContract(lenderToken, borrowerToken);
//        String loanId = getLatestContractId(pvReturnsToken, "OPEN");

//        String loanId = submitNewContract(lenderToken);
//        testReturnAcknowledge(lenderToken, "POSITIVE");
//        testReturnAcknowledge(pvReturnsToken, "POSITIVE");
//        testReturnCancel(pvReturnsToken);
//        testReturnCancel(borrowerToken);

//        submitNewReturn(pvReturnsToken, loanId);

        String loanId = "2c6eceac-f375-49de-b575-024edc115372";
        String rerateId = submitNewRerate(lenderToken, loanId);
//        String rerateId = "de9a3851-e280-4f40-99e3-d11c299e5ec2";

        // NOTE: The rerate is not created immediately... it seems we need to wait a few seconds before approving it!!
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        approveRerate(borrowerToken, loanId, rerateId);

        // Now wait for the rerate to be applied...
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        apiHelper.getEntity(lenderToken, "events"); //, queryParams);

//        apiHelper.getEntity(lenderToken, "rerates", queryParams);
//        submitNewRecall(lenderToken, loanId);

//        apiHelper.getEntity(borrowerToken, "loans", loanId);
//        apiHelper.getEntity(pvReturnsToken, "loans", loanId);
//        apiHelper.getEntity(lenderToken, "returns", queryParams);

//        ApiDemoApp apiDemoApp = new ApiDemoApp();
//        apiDemoApp.loadDelegations(borrowerToken);

        System.out.println("Done");
    }

    private static void testReturnAcknowledge(String token, String ackType) {

        // Find the latest (most recent) return with status "PENDING"
        JSONObject returnObj = getLatestReturnId(token, "PENDING");

        if (returnObj == null) {
            System.out.println("No returns found!");
            return;
        }
        acknowledgeReturn(token, returnObj, ackType);
    }

    private static void testReturnCancel(String token) {

        // Find the latest (most recent) return with status "PENDING"
        JSONObject returnObj = getLatestReturnId(token, "PENDING");

        if (returnObj == null) {
            System.out.println("No returns found!");
            return;
        }
        cancelReturn(token, returnObj);
    }

    private void loadDelegations(String token) {
        List<Delegation> dlgCache = cacheDelegations(token);

        if(dlgCache.size() > 0) {
            System.out.println(dlgCache.size() + " delegations found");
        }
        else {
            System.out.println("No delegations found");
        }
    }

    private List<Delegation>  cacheDelegations(String token) {
        List<Delegation> delegations = new ArrayList<>();

        String json = apiHelper.getEntity(token, "delegations");

        if (!json.isEmpty() && json.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(json);      // Array of delegations

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dlg = jsonArray.getJSONObject(i);

                if ("APPROVED".equals(dlg.getString("delegationStatus"))) {
//                    JSONArray parties = new JSONArray(dlg.getString("parties"));
                    JSONArray parties = (JSONArray) dlg.get("parties");
                    JSONArray authorizations = (JSONArray) dlg.get("authorizations");
                    JSONObject delegationParty = (JSONObject) dlg.get("delegationParty");

                    List<String> authList = new ArrayList<>();

                    authorizations.toList().forEach(auth -> authList.add((String) auth));

                    if (parties.length() == 2) {
                        String partyId1 = parties.getJSONObject(0).getString("partyId");
                        String partyId2 = parties.getJSONObject(1).getString("partyId");
                        String delegateTo = delegationParty.getString("partyId");

                        if (partyId1 != null && partyId2 != null) {
                            Delegation delegation = new Delegation(partyId1, partyId2, delegateTo, authList);
                            System.out.println("Found Delegation: " + delegation);

                            if (delegations.contains(delegation)) {
                                System.out.println("Skipping Delegation " + dlg.getString("delegationId")
                                        + ", already exists in cache");
                            }
                            else {
                                delegations.add(delegation);
                            }

                            if (!delegations.contains(delegation)) {
                                delegations.add(delegation);
                            }
                        }
                    }
                    else {
                        System.out.println("Skipping Delegation " + dlg.getString("delegationId")
                                            + ", does not have eaxctly 2 parties");
                    }

                    for (int j = 0; j < parties.length(); j++) {
                        JSONObject party = parties.getJSONObject(j);
                        String partyId = party.getString("partyId");
                        System.out.println("PartyId: " + partyId);
                    }
                }
            }
        }
        return delegations;
    }

    private static String createOpenContract(String lenderToken, String borrowerToken) {

        String contractId = submitNewContract(lenderToken);

        if (StringUtils.isEmpty(contractId)) {
            System.out.println("No contractId found!");
        }
        else {
            System.out.println("Created new loanId: " + contractId);

            approveContract(borrowerToken, contractId);     // Set "SETTLED"
            updateLoanSettled(lenderToken, contractId);
            updateLoanSettled(borrowerToken, contractId);
        }
        return contractId;
    }

    private static String submitNewContract(String lenderToken) {
        JSONObject body = JsonGenerator.generateContractProposalJson();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("\nSubmit contract:\n" + body.toString(3));
        ResponseEntity<LedgerResponseDTO> response = apiHelper.postContractProposal(lenderToken, body.toString());

        String contractId = null;

        if (response != null) {
            String resourceUri = response.getBody().getResourceUri();
            contractId = resourceUri.substring(resourceUri.lastIndexOf("/") + 1);
            System.out.println("loanId: " + contractId);
        }
        return contractId;
    }

    private static void approveContract(String borrowerToken, String contractId) {
        JSONObject body = JsonGenerator.generateContractApprovalJson(contractId);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Approve contract:\n" + body.toString(3));
        apiHelper.postContractAction(borrowerToken, body.toString(), contractId, "approve");
    }

    private static void approveRerate(String token, String loanId, String rerateId) {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Approve rerate");
        apiHelper.postEntityAction(token, null, loanId, "rerates", rerateId, "approve");
    }

    private static void updateLoanSettled(String token, String contractId) {
        JSONObject body = JsonGenerator.generateSettlementStatusUpdateJson("SETTLED");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Update contract status: " + body.toString(3));
        apiHelper.patchContract(token, body.toString(), contractId);
    }

    private static void submitNewReturn(String token, String contractId) {
        try {
            String submitter = getSubmitter(token);
            JSONObject body = JsonGenerator.generateReturnProposalJson(submitter, contractId);
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Submit return as " + submitter + ":\n" + body.toString(3));
            apiHelper.postReturnRecallProposal(token, body.toString(), contractId, "returns");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateReturnSettled(String token, String contractId, String returnId) {
        JSONObject body = JsonGenerator.generateSettlementStatusUpdateJson("SETTLED");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Update return status: " + body.toString(3));
        apiHelper.patchReturn(token, body.toString(), contractId, returnId);
    }

    private static void submitNewRecall(String token, String contractId) {
        JSONObject body = JsonGenerator.generateRecallProposalJson(contractId);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Submit recall as" + getSubmitter(token) + ":\n" + body.toString(3));

        apiHelper.postReturnRecallProposal(token, body.toString(), contractId, "recalls");
    }

    private static String submitNewRerate(String token, String contractId) {
        String rerateId = null;

        try {
            JSONObject body = JsonGenerator.generateRerateProposalJson(contractId);
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Submit rerate as " + getSubmitter(token) + ":\n" + body.toString(3));

            ResponseEntity<LedgerResponseDTO> response = apiHelper.postRerate(token, body.toString(), contractId, "rerates");


            if (response != null) {
                String resourceUri = response.getBody().getResourceUri();
                rerateId = resourceUri.substring(resourceUri.lastIndexOf("/") + 1);
                System.out.println("Created rerateId: " + rerateId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rerateId;
    }

    private static void acknowledgeReturn(String token, JSONObject returnObj, String ackType) {
        try {
            String returnId   = returnObj.getString("returnId");
            String contractId = returnObj.getString("contractId");

            JSONObject body = JsonGenerator.generateReturnAckJson(returnObj, ackType);
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Submit " + ackType + " return ACKNOWLEDGE as " + getSubmitter(token) + ":\n" + body.toString(3));
            apiHelper.postReturnAcknowledge(token, body.toString(), contractId, returnId, ackType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cancelReturn(String token, JSONObject returnObj) {
        try {
            String returnId   = returnObj.getString("returnId");
            String contractId = returnObj.getString("contractId");

            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Submit return CANCEL as " + getSubmitter(token) + " for contractId " + contractId + " and returnId " + returnId);
            apiHelper.postReturnCancel(token, contractId, returnId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the latest 1Source loan with the specfied status
     */
    private static String getLatestContractId(String lenderToken, String findStatus) {
        String contractId = null;

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("size", "20");

        String json = apiHelper.getEntity(lenderToken, "loans");

        if (!json.isEmpty() && json.startsWith("[")) {
            String latestEventDateTime = "0000-00-00T00:00:00.000";

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String updateDateTime = jsonObject.getString("lastUpdateDateTime");
                String contractStatus = jsonObject.getString("loanStatus");

                if (updateDateTime.compareTo(latestEventDateTime) > 0 && findStatus.equals(contractStatus)) {
                    latestEventDateTime = updateDateTime;
                    contractId = jsonObject.getString("loanId");
                    System.out.println("Found latest contractId: " + contractId);
                    break;
                }
            }
        }
        if (contractId == null) {
            System.out.println("getLatestContractId: No contracts found!");
            return null;
        }
        return contractId;
    }

    private static JSONObject getLatestReturnId(String token, String findStatus) {
        String returnId = null;
        JSONObject returnJson = null;

        String json = apiHelper.getEntity(token, "returns");

        if (!json.isEmpty() && json.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(json);

            System.out.println("Found " + jsonArray.length() + " returns");

            for (int i = 0; i < jsonArray.length(); i++) {
                returnJson = jsonArray.getJSONObject(i);

                String returnStatus = returnJson.getString("status");

                if (findStatus.equals(returnStatus)) {
                    JSONObject executionVenue = returnJson.getJSONObject("executionVenue");
                    JSONObject party = executionVenue.getJSONObject("party");
                    String partyId = party.getString("partyId");

                    if (PV_EQLREC.equals(partyId)) {
                        returnId = returnJson.getString("returnId");
                        System.out.println("Found returnId: " + returnId);
                        break;
                    }
                    else {
                        System.out.println("Skipping returnId " + returnJson.getString("returnId") + ", venue partyId is " + partyId);
                    }
                }
                else  {
                    System.out.println("Skipping returnId " + returnJson.getString("returnId") + ", status is " + returnStatus);
                }
                returnJson = null;
            }
        }
        else {
            System.out.println("getLatestReturnId: No returns found!");
            return null;
        }
        return returnJson;
    }

    public static String getSubmitter(String token) {
        String submitter = null;

        if (tokenMap != null) {
            for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.equals(token)) {
                    submitter = key;
                    break;
                }
            }
        }
        return submitter;
    }
}
