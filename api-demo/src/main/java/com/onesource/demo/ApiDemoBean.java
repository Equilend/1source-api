package com.onesource.demo;

import com.onesource.demo.util.ApiHelper;
import com.onesource.demo.util.JsonGenerator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class ApiDemoBean {

    private static ApiHelper apiHelper = new ApiHelper();

    @Bean
    public void demoRunner()  {
        String borrowerToken = apiHelper.getAuthToken("TestBorrower1User", "FqnNQyUwaenQ8K3h");
        String lenderToken = apiHelper.getAuthToken("TestLender1User", "fjmxVeKzpzUDg3YJ");

//        String sinceTime = "2024-06-05T01:00:00.000Z";
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(10);
        String sinceTime = localDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println("sinceTime " + sinceTime);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("since", sinceTime);

//        apiHelper.getEntity(lenderToken, "parties");
//        apiHelper.getEntity(lenderToken, "agreements");
//        apiHelper.getEntity(lenderToken, "delegations"); // Does not work... not implemented yet
        apiHelper.getEntity(lenderToken, "events", queryParams);
        apiHelper.getEntity(lenderToken, "contracts", queryParams);
        apiHelper.getEntity(lenderToken, "returns", queryParams);
//        apiHelper.getEntity(lenderToken, "recalls", queryParams);

//        submitNewContract(lenderToken);

//        approveContract(borrowerToken, "cf796f2f-9d68-4efe-b5d4-887223f48d9a");
//        submitNewReturn(borrowerToken, "a5ee63ed-2d63-4505-9478-97ab41d05b7e");

        runFullDemo(lenderToken, borrowerToken, queryParams);
        System.out.println("Done");
    }

    private static void runFullDemo(String lenderToken, String borrowerToken, MultiValueMap<String, String> queryParams) {

        submitNewContract(lenderToken);

        // Get new events and look for the latest contract in PROPOSED state,
        // it's probably be the one we just submitted... so we'll try to approve it and then submit a return
        String contractId = getLatestContractId(lenderToken);

        if (StringUtils.isEmpty(contractId)) {
            System.out.println("No contractId found!");
        }
        else {
            System.out.println("Found contractId: " + contractId);

            approveContract(borrowerToken, contractId);
            patchContract(borrowerToken, contractId);
            patchContract(lenderToken, contractId);
            submitNewReturn(borrowerToken, contractId);
        }

        apiHelper.getEntity(lenderToken, "contracts", queryParams);
        apiHelper.getEntity(lenderToken, "returns", queryParams);
    }

    private static void submitNewContract(String lenderToken) {
        JSONObject body = JsonGenerator.generateContractProposalJson();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("\nSubmit contract:\n" + body.toString(2));
        apiHelper.postContractProposal(lenderToken, body.toString());
    }

    private static void approveContract(String borrowerToken, String contractId) {
        JSONObject body = JsonGenerator.generateContractApprovalJson(contractId);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Approve contract:\n" + body.toString(2));
        apiHelper.postContractAction(borrowerToken, body.toString(), contractId, "approve");
    }

    private static void patchContract(String token, String contractId) {
        JSONObject body = JsonGenerator.generateSettlementStatusUpdateJson("SETTLED");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Update contract status:\n" + body.toString(2));
        apiHelper.patchContract(token, body.toString(), contractId);
    }

    private static void submitNewReturn(String borrowerToken, String contractId) {
        JSONObject body = JsonGenerator.generateReturnProposalJson(contractId);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Submit return:\n" + body.toString(2));
        apiHelper.postReturnRecallProposal(borrowerToken, body.toString(), contractId, "returns");
    }

    /**
     * Get the latest 1Source events and take the contractId from the most recent CONTRACT_PROPOSAL event.
     * This won't be needed once the API is modified to return the resourceUri when a new contract is posted.
     */
    private static String getLatestContractId(String lenderToken) {
        String contractId = null;

        String json = apiHelper.getEntity(lenderToken, "events");

        if (!json.isEmpty() && json.startsWith("[")) {
            String latestEventDateTime = "0000-00-00T00:00:00.000Z";

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String eventDateTime = jsonObject.getString("eventDateTime");
                String eventType = jsonObject.getString("eventType");

                if (eventDateTime.compareTo(latestEventDateTime) > 0 && eventType.equals("CONTRACT_PROPOSED")) {
                    latestEventDateTime = eventDateTime;
                    String resourceUri = jsonObject.getString("resourceUri");
                    System.out.println("Found resourceUri: " + resourceUri);
                    contractId = resourceUri.substring(resourceUri.lastIndexOf("/") + 1);
                }
            }
        }
        else {
            System.out.println("getLatestContractId: No events found!");
            return null;
        }
        return contractId;
    }
}
