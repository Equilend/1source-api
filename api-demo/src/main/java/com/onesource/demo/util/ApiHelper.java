package com.onesource.demo.util;

import com.intellecteu.onesource.integration.services.client.onesource.invoker.ApiClient;
import com.onesource.demo.dto.LedgerResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiHelper {

    private HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    public String getAuthToken(String userName, String password) {
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("auth_type", "BEARER"); // Cloned these from Python app
            formData.put("client_id", "canton-participant1-client");
            formData.put("client_secret", "c0a05c2d-ac70-472a-ac4f-38b80dba28d8");
            formData.put("grant_type", "password");
            formData.put("username", userName);
            formData.put("password", password);

            HttpRequest request = HttpRequest
                    .newBuilder(new URI("https://stageauth.equilend.com/auth/realms/1Source/protocol/openid-connect/token"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
                    .build();

            System.out.println(request.method() + " " + request.uri() + "\nHeaders: " + request.headers() + "\n");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject obj = new JSONObject(response.body());
            return obj.getString("access_token");
        }
        catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEntity(String token, String entity) {
        return getEntity(token, entity, null);
    }

    public String getEntity(String token, String entity, String conditions) {
        System.out.println("------------------------------------------------------------------------------------");
        HttpRequest request = null;
        HttpResponse<String> response = null;

        String url = "https://stageapi.equilend.com/v1/ledger/" + entity + (conditions == null ? "" : "?" + conditions);

        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
//                    .version(HttpClient.Version.HTTP_2)
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .build();

            // Compare to: apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
            // from EventsApi:177
            System.out.println("\nGET " + StringUtils.capitalize(entity) + "  " + url + "\n");

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Success");
            }
            else {
                System.out.println("GET " + entity + " request failed with status " + response.statusCode() + " " + response);

                if (response.body() != null) {
                    System.out.println("response: " + response.body());
                }
                return url;
            }
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String body = response.body();
        System.out.println("response: " + body);

        String resultStr = ppJson(body);

        System.out.println(StringUtils.capitalize(entity) + ":\n" + resultStr);
        return body;
    }

    public ResponseEntity<LedgerResponseDTO> postContractProposal(String token, String body) {
        return postToApi(token, body, null, null, null, null, null);  // Contract Proposal
    }

    public ResponseEntity<LedgerResponseDTO> postContractAction(String token, String body, String contractId, String contractAction) {
        return postToApi(token, body, contractId, contractAction, null, null, null);  // Approve, Cancel, Decline a contract
    }

    public ResponseEntity<LedgerResponseDTO> postReturnRecallProposal(String token, String body, String contractId, String entity) {
        return postToApi(token, body, contractId, null, entity, null, null);  // Post a new return or recall proposal
    }

    /**
     * @param token          = borrowerToken for return, lenderToken for recall
     * @param body
     * @param contractId     = contract being approved or returned or recalled
     * @param contractAction = approve
     * @param entity         = "returns" or "recalls"
     * @param entityId       = returnId or recallId
     * @param entityAction   = "acknowlege" (return) or "cancel" (recall)
     * @return
     */
    private ResponseEntity<LedgerResponseDTO> postToApi(String token, String body, String contractId, String contractAction,
                                                              String entity, String entityId, String entityAction) {
        ApiClient apiClient = new ApiClient();

        if (body == null) {
            throw new RuntimeException("Missing required parameter 'body'");
        }

        String pathStr = "/ledger/contracts";
        StringBuffer args = new StringBuffer();

        final Map<String, Object> uriVariables = new HashMap<String, Object>();

        if (contractId != null) {
            pathStr += "/{contractId}";
            uriVariables.put("contractId", contractId);
            args.append("contractId=" + contractId);
        }

        if (contractAction != null) {
            pathStr += "/{contractAction}";
            uriVariables.put("contractAction", contractAction);
            args.append(", contractAction=" + contractAction);
        }

        if (entity != null) {
            pathStr += "/{entity}";
            uriVariables.put("entity", entity);
            args.append(", entity=" + entity);
        }

        if (entityId != null) {
            pathStr += "/{entityId}";
            uriVariables.put("entityId", entityId);
            args.append(", entityId=" + entityId);
        }

        if (entityAction != null) {
            pathStr += "/{entityAction}";
            uriVariables.put("entityAction", entityAction);
            args.append(", entityAction=" + entityAction);
        }

        String path = UriComponentsBuilder.fromPath(pathStr).buildAndExpand(uriVariables).toUriString();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();

        HttpHeaders headerParams = new HttpHeaders();
        headerParams.add("Authorization", "Bearer " + token);

        MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        String[] accepts = {"application/json"};

        List<MediaType> accept = apiClient.selectHeaderAccept(accepts);

        String[] contentTypes = {"application/json"};
        MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"stage_auth"};

        ParameterizedTypeReference<LedgerResponseDTO> returnType = new ParameterizedTypeReference<LedgerResponseDTO>() {};

        // TODO: Move the invokeAPI code inline here for simpler example...
        ResponseEntity<LedgerResponseDTO> responseEntity = apiClient.invokeAPI(path, HttpMethod.POST, queryParams, body, headerParams, formParams, accept, contentType, authNames, returnType);

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Post to 1source submitted successfully:\n" + responseEntity.getBody());
        }
        else {
            System.out.println("Post to 1source failed with status " + responseEntity.getStatusCode() + " " + responseEntity);
        }
        return responseEntity;
    }

    /**
     * If the JSON string represents an array, this method returns the JSON with each element
     * of the array on a new line.
     */
    private String ppJson(String json) {
        String formattedJson;

        if (!json.isEmpty() && json.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(json);

            StringBuilder sbuff = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                sbuff.append(jsonObject.toString()).append("\n");
            }
            formattedJson = sbuff.toString();
        }
        else {
            formattedJson = new JSONObject(json).toString();
        }

        if (formattedJson == null || formattedJson.trim().isEmpty()) {
            formattedJson = " (NONE)";
        }
        return formattedJson;
    }

    private String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        System.out.println("getFormDataAsString: " + formBodyBuilder.toString());
        return formBodyBuilder.toString();
    }
}
