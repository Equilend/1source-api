package com.onesource.demo.util;

import com.intellecteu.onesource.integration.services.client.onesource.invoker.auth.Authentication;
import com.intellecteu.onesource.integration.services.client.onesource.invoker.auth.OAuth;
import com.onesource.demo.dto.LedgerResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiHelper {

    private HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private String basePath = "https://stageapi.equilend.com/v1";
    private RestTemplate restTemplate;

    public ApiHelper() {
        restTemplate = new RestTemplate();

        // This allows us to read the response more than once - Necessary for debugging.
        HttpComponentsClientHttpRequestFactory httpComponentsFactory = new HttpComponentsClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(httpComponentsFactory);
        restTemplate.setRequestFactory(bufferingFactory);
//        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));

        restTemplate.getInterceptors().add(new RequestLoggingInterceptor());
    }

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

        String url = basePath + "/ledger/" + entity + (conditions == null ? "" : "?" + conditions);

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
        return postToApi(HttpMethod.POST, token, body, null, null, null, null, null);  // Contract Proposal
    }

    public ResponseEntity<LedgerResponseDTO> postContractAction(String token, String body, String contractId, String contractAction) {
        return postToApi(HttpMethod.POST, token, body, contractId, contractAction, null, null, null);  // Approve, Cancel, Decline a contract
    }

    public ResponseEntity<LedgerResponseDTO> patchContract(String token, /*ContractsContractIdBodyDTO*/ String body, String contractId) {
        return postToApi(HttpMethod.PATCH, token, body, contractId, null, null, null, null);
//        return submitPatch(token, contractId, body);
    }

    public ResponseEntity<LedgerResponseDTO> postReturnRecallProposal(String token, String body, String contractId, String entity) {
        return postToApi(HttpMethod.POST, token, body, contractId, null, entity, null, null);  // Post a new return or recall proposal
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
    private ResponseEntity<LedgerResponseDTO> postToApi(HttpMethod httpMethod, String token, String body, String contractId, String contractAction,
                                                        String entity, String entityId, String entityAction) {
        if (body == null) {
            throw new RuntimeException("Missing required parameter 'body'");
        }

        String pathStr = "/ledger/contracts";

        final Map<String, Object> uriVariables = new HashMap<String, Object>();

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

        ResponseEntity<LedgerResponseDTO> responseEntity = getApiResponse(httpMethod, token, body, path);

        return responseEntity;
    }

    private ResponseEntity<LedgerResponseDTO> getApiResponse(HttpMethod httpMethod, String token, String body, String path) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();

        HttpHeaders headerParams = new HttpHeaders();
        headerParams.add("Authorization", "Bearer " + token);

        String[] accepts = { "application/json" };
        List<MediaType> accept = selectHeaderAccept(accepts);

        String[] contentTypes = { "application/json" };
        MediaType contentType = selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "stage_auth" };

        updateParamsForAuth(authNames, queryParams, headerParams);

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basePath).path(path);

        if (queryParams != null) {
            builder.queryParams(queryParams);
        }

        final RequestEntity.BodyBuilder requestBuilder = RequestEntity.method(httpMethod, builder.build().toUri());
        if(accept != null) {
            requestBuilder.accept(accept.toArray(new MediaType[accept.size()]));
        }
        if(contentType != null) {
            requestBuilder.contentType(contentType);
        }

        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.add("User-Agent","Java-SDK");

        addHeadersToRequest(headerParams, requestBuilder);
        addHeadersToRequest(defaultHeaders, requestBuilder);

        MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        RequestEntity<Object> requestEntity = requestBuilder.body(selectBody(body, formParams, contentType));

//        System.out.println("Request Method: " + requestEntity.getMethod());
//        System.out.println("Request URI: " + requestEntity.getURI());
//        System.out.println("Request Headers: " + requestEntity.getHeaders());
//        System.out.println("Request Body: " + body); // new String(body, StandardCharsets.UTF_8));

        ParameterizedTypeReference<LedgerResponseDTO> returnType = new ParameterizedTypeReference<LedgerResponseDTO>() {};
        ResponseEntity<LedgerResponseDTO> responseEntity = restTemplate.exchange(requestEntity, returnType);

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
        Map<String, Authentication>  authentications = new HashMap<String, Authentication>();
        authentications.put("stage_auth", new OAuth());

        // Prevent the authentications from being modified.
        authentications = Collections.unmodifiableMap(authentications);

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
