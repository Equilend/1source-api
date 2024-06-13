# RecallsApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerContractsContractIdRecallsGet**](RecallsApi.md#ledgerContractsContractIdRecallsGet) | **GET** /ledger/contracts/{contractId}/recalls | Read collection of recalls against contract specified by &#x27;contractId&#x27;
[**ledgerContractsContractIdRecallsPost**](RecallsApi.md#ledgerContractsContractIdRecallsPost) | **POST** /ledger/contracts/{contractId}/recalls | Initiates a recall
[**ledgerContractsContractIdRecallsRecallIdCancelPost**](RecallsApi.md#ledgerContractsContractIdRecallsRecallIdCancelPost) | **POST** /ledger/contracts/{contractId}/recalls/{recallId}/cancel | Cancel a recall in \&quot;proposed\&quot; state. Original proposer only.
[**ledgerContractsContractIdRecallsRecallIdGet**](RecallsApi.md#ledgerContractsContractIdRecallsRecallIdGet) | **GET** /ledger/contracts/{contractId}/recalls/{recallId} | Read a recall
[**ledgerContractsContractIdRecallsRecallIdVenuecancelPost**](RecallsApi.md#ledgerContractsContractIdRecallsRecallIdVenuecancelPost) | **POST** /ledger/contracts/{contractId}/recalls/{recallId}/venuecancel | Cancel a recall in \&quot;proposed\&quot; state. Original proposer only.
[**ledgerContractsContractIdVenuerecallsPost**](RecallsApi.md#ledgerContractsContractIdVenuerecallsPost) | **POST** /ledger/contracts/{contractId}/venuerecalls | For delegated venues to create a recall atomically
[**ledgerRecallsGet**](RecallsApi.md#ledgerRecallsGet) | **GET** /ledger/recalls | Read collection of recalls
[**ledgerRecallsRecallIdGet**](RecallsApi.md#ledgerRecallsRecallIdGet) | **GET** /ledger/recalls/{recallId} | Read a recall

<a name="ledgerContractsContractIdRecallsGet"></a>
# **ledgerContractsContractIdRecallsGet**
> Recalls ledgerContractsContractIdRecallsGet(contractId)

Read collection of recalls against contract specified by &#x27;contractId&#x27;

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    Recalls result = apiInstance.ledgerContractsContractIdRecallsGet(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerContractsContractIdRecallsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**Recalls**](Recalls.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdRecallsPost"></a>
# **ledgerContractsContractIdRecallsPost**
> ledgerContractsContractIdRecallsPost(body, contractId)

Initiates a recall

Initiates a recall notice to return securities on a contract.  ### Validation rules:   - Caller must be the lender on the contract   - &#x60;contractId&#x60; -- must reference an &#x60;OPEN&#x60; contract   - &#x60;executionVenue&#x60;     - is mandatory     - if &#x60;executionVenue.type&#x60; &#x3D;&#x3D; &#x60;ONPLATFORM&#x60; then all other elements in &#x60;executionVenue&#x60; must be null     - if &#x60;executionVenue.type&#x60; &#x3D;&#x3D; &#x60;OFFPLATFORM&#x60; then &#x60;executionVenue.partyId&#x60;, &#x60;executionVenue.venueName&#x60;, and &#x60;executionVenue.partyRole&#x60; are mandatory   - &#x60;quantity&#x60;     - is mandatory     - must be &lt; 0     - must be &lt;&#x3D; contract open quantity - all existing open recall quantities   - &#x60;recallDate&#x60;     - is mandatory     - must be &gt;&#x3D; current day   - &#x60;recallDueDate&#x60;     - is mandatory     - must be &gt; &#x60;recallDate&#x60; 

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
RecallProposal body = new RecallProposal(); // RecallProposal | New recall proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    apiInstance.ledgerContractsContractIdRecallsPost(body, contractId);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerContractsContractIdRecallsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RecallProposal**](RecallProposal.md)| New recall proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdRecallsRecallIdCancelPost"></a>
# **ledgerContractsContractIdRecallsRecallIdCancelPost**
> LedgerResponse ledgerContractsContractIdRecallsRecallIdCancelPost(contractId, recallId)

Cancel a recall in \&quot;proposed\&quot; state. Original proposer only.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String recallId = "recallId_example"; // String | The unique identifier of a recall
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdRecallsRecallIdCancelPost(contractId, recallId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerContractsContractIdRecallsRecallIdCancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **recallId** | [**String**](.md)| The unique identifier of a recall |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdRecallsRecallIdGet"></a>
# **ledgerContractsContractIdRecallsRecallIdGet**
> Recall ledgerContractsContractIdRecallsRecallIdGet(contractId, recallId)

Read a recall

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String recallId = "recallId_example"; // String | The unique identifier of a recall
try {
    Recall result = apiInstance.ledgerContractsContractIdRecallsRecallIdGet(contractId, recallId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerContractsContractIdRecallsRecallIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **recallId** | [**String**](.md)| The unique identifier of a recall |

### Return type

[**Recall**](Recall.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdRecallsRecallIdVenuecancelPost"></a>
# **ledgerContractsContractIdRecallsRecallIdVenuecancelPost**
> LedgerResponse ledgerContractsContractIdRecallsRecallIdVenuecancelPost(contractId, recallId)

Cancel a recall in \&quot;proposed\&quot; state. Original proposer only.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String recallId = "recallId_example"; // String | The unique identifier of a recall
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdRecallsRecallIdVenuecancelPost(contractId, recallId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerContractsContractIdRecallsRecallIdVenuecancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **recallId** | [**String**](.md)| The unique identifier of a recall |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdVenuerecallsPost"></a>
# **ledgerContractsContractIdVenuerecallsPost**
> ledgerContractsContractIdVenuerecallsPost(body, contractId)

For delegated venues to create a recall atomically

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
RecallProposal body = new RecallProposal(); // RecallProposal | New recall proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    apiInstance.ledgerContractsContractIdVenuerecallsPost(body, contractId);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerContractsContractIdVenuerecallsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RecallProposal**](RecallProposal.md)| New recall proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerRecallsGet"></a>
# **ledgerRecallsGet**
> Recalls ledgerRecallsGet()

Read collection of recalls

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
try {
    Recalls result = apiInstance.ledgerRecallsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerRecallsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Recalls**](Recalls.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerRecallsRecallIdGet"></a>
# **ledgerRecallsRecallIdGet**
> Recall ledgerRecallsRecallIdGet(recallId)

Read a recall

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.RecallsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

RecallsApi apiInstance = new RecallsApi();
String recallId = "recallId_example"; // String | The unique identifier of a recall
try {
    Recall result = apiInstance.ledgerRecallsRecallIdGet(recallId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RecallsApi#ledgerRecallsRecallIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **recallId** | [**String**](.md)| The unique identifier of a recall |

### Return type

[**Recall**](Recall.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

