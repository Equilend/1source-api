package com.onesource.demo.util;

import com.intellecteu.onesource.integration.services.client.onesource.invoker.auth.Authentication;
import com.intellecteu.onesource.integration.services.client.onesource.invoker.auth.OAuth;
import com.intellecteu.onesource.integration.services.client.onesource.invoker.auth.RFC3339DateFormat;
import com.onesource.demo.ApiDemoApp;
import com.onesource.demo.dto.LedgerResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.*;

public class ApiHelper {

    private HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private String basePath = "https://stageapi.equilend.com/v1";
    private RestTemplate restTemplate;
    private Map<String, Authentication> authentications;
    private DateFormat dateFormat;
    private HttpHeaders defaultHeaders = new HttpHeaders();
    private String refreshToken = null;

    public ApiHelper() {
        restTemplate = new RestTemplate();

        // Buffering allows us to read the response more than once - Necessary for debugging.
        HttpComponentsClientHttpRequestFactory httpComponentsFactory = new HttpComponentsClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(httpComponentsFactory);
        restTemplate.setRequestFactory(bufferingFactory);
        restTemplate.getInterceptors().add(new RequestLoggingInterceptor());

        // Use RFC3339 format for date and datetime.
        // See http://xml2rfc.ietf.org/public/rfc/html/rfc3339.html#anchor14
        dateFormat = new RFC3339DateFormat();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        defaultHeaders.add("User-Agent", "Java-SDK");

        // Setup authentications (key: authentication name, value: authentication).
        authentications = new HashMap<String, Authentication>();
        authentications.put("stage_auth", new OAuth());
        // Prevent the authentications from being modified.
        authentications = Collections.unmodifiableMap(authentications);
    }

    public String getAuthToken(String userName, String password) {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Acquire Auth token for " + userName);
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

            System.out.println(request.method() + " " + request.uri() );
//            System.out.println(request.method() + " " + request.uri() + "\nRequest Headers: ");
//            request.headers().map().forEach((k, v) -> System.out.println("    " + k + ": " + v));

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to get token for " + userName + ": " + response.statusCode() + " " + response.body());
                return null;
            }

//            System.out.println("Response Headers: ");
//            response.headers().map().forEach((k, v) -> System.out.println("    " + k + ": " + v));

            JSONObject obj = new JSONObject(response.body());
//            System.out.println("Response for " + userName + ":\n" + obj.toString(4));

            String token     = obj.getString("access_token");
            Integer expiresIn = obj.getInt("expires_in");
            System.out.println("Token acquired for " + userName + ", expires in " + expiresIn + " seconds");

            if (refreshToken == null && obj.has("refresh_token")) {
                refreshToken = obj.getString("refresh_token");
            }
            return token;
        }
        catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String refreshAuthToken() {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Refresh token...");
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("grant_type", "refresh_token");
            formData.put("refresh_token", refreshToken);
            formData.put("client_id", "canton-participant1-client"); // Use your actual client ID
            formData.put("client_secret", "c0a05c2d-ac70-472a-ac4f-38b80dba28d8"); // Use your actual client secret

            HttpRequest request = HttpRequest.newBuilder(new URI("https://stageauth.equilend.com/auth/realms/1Source/protocol/openid-connect/token"))
                    .version(HttpClient.Version.HTTP_2)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to refresh token: " + response.statusCode() + " " + response.body());
                return null;
            }

            JSONObject obj = new JSONObject(response.body());
            System.out.println("Refresh rsponse:\n" + obj.toString(4) + "\n");

            String newAccessToken = obj.getString("access_token");
            System.out.println("New access token: " + newAccessToken);
            return newAccessToken;
        }
        catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEntity(String token, String entity) {
        return getEntity(token, entity, null, null);
    }

    public String getEntity(String token, String entity, MultiValueMap<String, String> queryParams) {
        return getEntity(token, entity, null, queryParams);
    }

    public String getEntity(String token, String entity, String entityId) {
        return getEntity(token, entity, entityId, null);
    }

    private String getEntity(String token, String entity, String entityId, MultiValueMap<String,String> queryParams) {
        if (token == null) {
            System.out.println("token is null, API is not initialized yet");
            return null;
        }

        String submitter = ApiDemoApp.getSubmitter(token);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath)
                .queryParams(queryParams)
                .path("/ledger/" + entity);
        if(entityId != null) {
            builder.path("/" + entityId);
        }

        RequestEntity.BodyBuilder requestBuilder = RequestEntity.method(HttpMethod.GET, builder.build().toUri());

        HttpHeaders headerParams = new HttpHeaders();
        headerParams.add("Authorization", "Bearer " + token);

        String[] accepts = { "application/json" };
        List<MediaType> accept = selectHeaderAccept(accepts);
        requestBuilder.accept(accept.toArray(new MediaType[accept.size()]));

        String[] contentTypes = { "application/json" };
        MediaType contentType = MediaType.APPLICATION_JSON;
        requestBuilder.contentType(contentType);

        String[] authNames = new String[] { "stage_auth" };

        updateParamsForAuth(authNames, queryParams, headerParams);

        addHeadersToRequest(headerParams, requestBuilder);
        addHeadersToRequest(defaultHeaders, requestBuilder);

        String body = "";
        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        RequestEntity<Object> requestEntity = requestBuilder.body(selectBody(body, /*formParams*/ null, contentType));

        System.out.println("GET " + StringUtils.capitalize(entity) + "  " + builder.toUriString() + "\n");

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, returnType);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {

            if (responseEntity.getStatusCode().value() == 404) {  // Not found
                System.out.println("API request GET " + entity + " failed 404 - NOT FOUND\n" + responseEntity);
            }
            else {
                System.out.println("API request GET " + entity + " failed with status " + responseEntity.getStatusCode() + "\n" + responseEntity);
            }

            if (responseEntity.hasBody()) {
                System.out.println("response: " + responseEntity.getBody());
            }
//            throw new HttpClientErrorException(responseEntity.getStatusCode(),
//                    "API returned " + responseEntity.getStatusCode()
//                            + " and it wasn't handled by the RestTemplate error handler");
            return null;
        }

        String responseBody = responseEntity.getBody();
//        System.out.println("response: " + responseBody);

        String resultStr = ppJson(responseBody);

        String msg = (submitter == null ? "" : " for " + submitter);
        System.out.println(StringUtils.capitalize(entity) + msg + ":\n" + resultStr);
        return responseBody;
    }

    private String getEntitySAVE(String token, String entity, MultiValueMap<String,String> queryParams, String tag) {
        System.out.println("------------------------------------------------------------------------------------");

        String path = basePath + "/ledger/" + entity;

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path("/ledger/" + entity);

        if (queryParams != null) {
            builder.queryParams(queryParams);
        }

        final RequestEntity.BodyBuilder requestBuilder = RequestEntity.method(HttpMethod.GET, builder.build().toUri());

        HttpHeaders headerParams = new HttpHeaders();
        headerParams.add("Authorization", "Bearer " + token);

        String[] accepts = { "application/json" };
        List<MediaType> accept = selectHeaderAccept(accepts);
        requestBuilder.accept(accept.toArray(new MediaType[accept.size()]));

        String[] contentTypes = { "application/json" };
//        MediaType contentType = selectHeaderContentType(contentTypes);
        MediaType contentType = MediaType.APPLICATION_JSON;
        requestBuilder.contentType(contentType);

        String[] authNames = new String[] { "stage_auth" };

        updateParamsForAuth(authNames, queryParams, headerParams);

        addHeadersToRequest(headerParams, requestBuilder);
        addHeadersToRequest(defaultHeaders, requestBuilder);

        String body = "";
        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        RequestEntity<Object> requestEntity = requestBuilder.body(selectBody(body, /*formParams*/ null, contentType));

        System.out.println("\nGET " + StringUtils.capitalize(entity) + "  " + path + "\n");

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, returnType);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Success");
        }
        else {
            System.out.println("GET " + entity + " request failed with status " + responseEntity.getStatusCode() + " " + responseEntity);

            if (responseEntity.hasBody()) {
                System.out.println("response: " + responseEntity.getBody());
            }
            return null;
            // The error handler built into the RestTemplate should handle 400 and 500 series errors.
//            throw new HttpClientErrorException(responseEntity.getStatusCode(),
//                              "API returned " + responseEntity.getStatusCode()
//                            + " and it wasn't handled by the RestTemplate error handler");
        }

        String responseBody = responseEntity.getBody();
        System.out.println("response: " + responseBody);

        String resultStr = ppJson(responseBody);

        String msg = (tag == null ? "" : " for " + tag);
        System.out.println(StringUtils.capitalize(entity) + msg + ":\n" + resultStr);
        return responseBody;
    }

    public ResponseEntity<LedgerResponseDTO> postContractProposal(String token, String body) {
        return submitApiPost(HttpMethod.POST, token, body, null, null, null, null, null);  // Contract Proposal
    }

    public ResponseEntity<LedgerResponseDTO> postContractAction(String token, String body, String contractId, String contractAction) {
        return submitApiPost(HttpMethod.POST, token, body, contractId, contractAction, null, null, null);  // Approve, Cancel, Decline a contract
    }

    public ResponseEntity<LedgerResponseDTO> postEntityAction(String token, String body, String contractId, String entity, String entityId, String entityAction) {
        return submitApiPost(HttpMethod.POST, token, body, contractId, null, entity, entityId, entityAction);  // Approve, Cancel, Decline a rerate, return or recall
    }

    public ResponseEntity<LedgerResponseDTO> patchContract(String token, String body, String contractId) {
        return submitApiPost(HttpMethod.PATCH, token, body, contractId, null, null, null, null); // Patch a contract (SettlementStatusUpdate, etc)
    }

    public ResponseEntity<LedgerResponseDTO> patchReturn(String token, String body, String contractId, String returnId) {
        return submitApiPost(HttpMethod.PATCH, token, body, contractId, null, "returns", returnId, null); // Patch a return (SettlementStatusUpdate, etc)
    }

    public ResponseEntity<LedgerResponseDTO> postReturnRecallProposal(String token, String body, String contractId, String entity) {
        return submitApiPost(HttpMethod.POST, token, body, contractId, null, entity, null, null);  // Post a new return or recall proposal
    }

    public ResponseEntity<LedgerResponseDTO> postReturnAcknowledge(String token, String body, String contractId, String returnId, String ackType) {
        return submitApiPost(HttpMethod.POST, token, body, contractId, null, "returns", returnId, "acknowledge");
    }

    public ResponseEntity<LedgerResponseDTO> postReturnCancel(String token, String contractId, String returnId) {
        return submitApiPost(HttpMethod.POST, token, null, contractId, null, "returns", returnId, "cancel");
    }

    public ResponseEntity<LedgerResponseDTO> postRerate(String token, String body, String contractId, String entity) {
            return submitApiPost(HttpMethod.POST, token, body, contractId, null, entity, null, null);
        }

    /**
     * @param httpMethod     = POST or PATCH
     * @param token          = borrowerToken for return, lenderToken for recall
     * @param body
     * @param contractId     = contract being approved or returned or recalled
     * @param contractAction = approve
     * @param entity         = "returns" or "recalls"
     * @param entityId       = returnId or recallId
     * @param entityAction   = "acknowlege" (return) or "cancel" (recall)
     * @return
     */
    private ResponseEntity<LedgerResponseDTO> submitApiPost(HttpMethod httpMethod, String token, String body, String contractId, String contractAction,
                                                            String entity, String entityId, String entityAction) {
        if (body == null && !"cancel".equals(entityAction) && !"approve".equals(entityAction)) {
            throw new RuntimeException("Missing required parameter 'body'");
        }

        String pathStr = "/ledger/loans";

        Map<String, Object> uriVariables = new HashMap<String, Object>();

        if (contractId != null) {
            pathStr += "/{contractId}";
            uriVariables.put("contractId", contractId);
        }

        if (contractAction != null) {
            pathStr += "/{contractAction}";
            uriVariables.put("contractAction", contractAction);
        }

        if (entity != null) {
            pathStr += "/{entity}";
            uriVariables.put("entity", entity);
        }

        if (entityId != null) {
            pathStr += "/{entityId}";
            uriVariables.put("entityId", entityId);
        }

        if (entityAction != null) {
            pathStr += "/{entityAction}";
            uriVariables.put("entityAction", entityAction);
        }

        String path = UriComponentsBuilder.fromPath(pathStr).buildAndExpand(uriVariables).toUriString();

//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        ParameterizedTypeReference<LedgerResponseDTO> returnType = new ParameterizedTypeReference<LedgerResponseDTO>() {};
        return getApiPostResponse(httpMethod, token, body, path, returnType);
    }

    private <T> ResponseEntity<T> getApiPostResponse(HttpMethod httpMethod, String token, String body,
                                                                 String path, ParameterizedTypeReference<T> returnType) {
        HttpHeaders headerParams = new HttpHeaders();
        headerParams.add("Authorization", "Bearer " + token);

        String[] accepts = { "application/json" };
        List<MediaType> accept = selectHeaderAccept(accepts);

        String[] contentTypes = { "application/json" };
        MediaType contentType = selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "stage_auth" };

        updateParamsForAuth(authNames, null, headerParams);

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path(path);

        RequestEntity.BodyBuilder requestBuilder = RequestEntity.method(httpMethod, builder.build().toUri());

        if(accept != null) {
            requestBuilder.accept(accept.toArray(new MediaType[accept.size()]));
        }
        if(contentType != null) {
            requestBuilder.contentType(contentType);
        }

        addHeadersToRequest(headerParams, requestBuilder);
        addHeadersToRequest(defaultHeaders, requestBuilder);

        RequestEntity<Object> requestEntity = requestBuilder.body(selectBody(body, null, contentType));

        ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntity, returnType);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println(httpMethod + " submitted successfully:\n" + responseEntity.getBody());
        }
        else {
            // The error handler built into the RestTemplate should handle 400 and 500 series errors.
            throw new HttpClientErrorException(responseEntity.getStatusCode(),
                    "API returned " + responseEntity.getStatusCode()
                            + " and it wasn't handled by the RestTemplate error handler");
        }
        return responseEntity;
    }

    public List<MediaType> selectHeaderAccept(String[] accepts) {
        if (accepts.length == 0) {
            return null;
        }
        for (String accept : accepts) {
            MediaType mediaType = MediaType.parseMediaType(accept);
            if (isJsonMime(mediaType)) {
                return Collections.singletonList(mediaType);
            }
        }
        return MediaType.parseMediaTypes(org.springframework.util.StringUtils.arrayToCommaDelimitedString(accepts));
    }

    public boolean isJsonMime(MediaType mediaType) {
        return mediaType != null && (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || mediaType.getSubtype().matches("^.*\\+json[;]?\\s*$"));
    }

    public MediaType selectHeaderContentType(String[] contentTypes) {
        if (contentTypes.length == 0) {
            return MediaType.APPLICATION_JSON;
        }
        for (String contentType : contentTypes) {
            MediaType mediaType = MediaType.parseMediaType(contentType);
            if (isJsonMime(mediaType)) {
                return mediaType;
            }
        }
        return MediaType.parseMediaType(contentTypes[0]);
    }

    protected Object selectBody(Object obj, MultiValueMap<String, Object> formParams, MediaType contentType) {
        boolean isForm = MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType) || MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType);
        return isForm ? formParams : obj;
    }

    private void updateParamsForAuth(String[] authNames, MultiValueMap<String, String> queryParams, HttpHeaders headerParams) {
//        Map<String, Authentication>  authentications = new HashMap<String, Authentication>();
//        authentications.put("stage_auth", new OAuth());
//
//        // Prevent the authentications from being modified.
//        authentications = Collections.unmodifiableMap(authentications);

        for (String authName : authNames) {
            Authentication auth = authentications.get(authName);
            if (auth == null) {
                throw new RestClientException("Authentication undefined: " + authName);
            }
            auth.applyToParams(queryParams, headerParams);
        }
    }

    protected void addHeadersToRequest(HttpHeaders headers, RequestEntity.BodyBuilder requestBuilder) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            List<String> values = entry.getValue();
            for(String value : values) {
                if (value != null) {
                    requestBuilder.header(entry.getKey(), value);
                }
            }
        }
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

    /**
     * convert a Map<String, String> to a URL-encoded form data string.
     */
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
//        System.out.println("getFormDataAsString: " + formBodyBuilder.toString());
        return formBodyBuilder.toString();
    }
}
