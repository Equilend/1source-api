package com.os.tester;

import com.os.client.api.ContractsApi;
import com.os.client.invoker.ApiClient;
import com.os.client.invoker.ApiException;
import com.os.client.invoker.Configuration;
import com.os.client.invoker.auth.OAuth;
import com.os.tester.util.ApiHelper;

import java.util.Calendar;
import java.util.Date;

public class ApiTester {

    public static void main(String[] args) {
        ApiHelper apiHelper = new ApiHelper();
        String borrowerToken = apiHelper.getAuthToken("TestBorrower1User", "FqnNQyUwaenQ8K3h");
        String lenderToken = apiHelper.getAuthToken("TestLender1User", "fjmxVeKzpzUDg3YJ");

        System.out.println("Got borrower token: " + borrowerToken);
        ApiClient apiClient = Configuration.getDefaultApiClient();

        // Configure OAuth2 access token for authorization: stage_auth
        OAuth stage_auth = (OAuth) apiClient.getAuthentication("stage_auth");
        stage_auth.setAccessToken(borrowerToken);

//        String sinceTime = "2024-06-05T01:00:00.000Z";
//        apiHelper.getEntity(lenderToken, "parties");
//        apiHelper.getEntity(lenderToken, "agreements");
//        apiHelper.getEntity(lenderToken, "delegations"); // Does not work... not implemented yet
//        apiHelper.getEntity(lenderToken, "events", "since=" + sinceTime);
//        apiHelper.getEntity(lenderToken, "contracts", "since=" + sinceTime);
//        apiHelper.getEntity(lenderToken, "returns", "since=" + sinceTime);
//        apiHelper.getEntity(lenderToken, "recalls", "since=" + sinceTime);

//        approveContract(borrowerToken, "cf796f2f-9d68-4efe-b5d4-887223f48d9a");
//        submitNewReturn(borrowerToken, "a5ee63ed-2d63-4505-9478-97ab41d05b7e");

        ContractsApi contractsApi = new ContractsApi();
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -10);
            Date fromDate = cal.getTime();
            Integer size = 56; // Integer | Number of contracts to be returned. Can be used to facilitate paging

            contractsApi.ledgerContractsGet(null,null,null,null,null,null,null,null,null,null,null);
//            contractsApi.ledgerContractsGet(fromDate,before,null,null,null,null,null,null,null,null,null);
        }
        catch (ApiException e) {
            e.printStackTrace();
        }

//        runFullDemo(lenderToken, borrowerToken, sinceTime);
        System.out.println("Done");
    }
}
