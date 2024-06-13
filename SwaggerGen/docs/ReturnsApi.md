# ReturnsApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerContractsContractIdReturnsGet**](ReturnsApi.md#ledgerContractsContractIdReturnsGet) | **GET** /ledger/contracts/{contractId}/returns | Read collection of returns against contract specified by &#x27;contractId&#x27;
[**ledgerContractsContractIdReturnsPost**](ReturnsApi.md#ledgerContractsContractIdReturnsPost) | **POST** /ledger/contracts/{contractId}/returns | Create a return
[**ledgerContractsContractIdReturnsReturnIdAcknowledgePost**](ReturnsApi.md#ledgerContractsContractIdReturnsReturnIdAcknowledgePost) | **POST** /ledger/contracts/{contractId}/returns/{returnId}/acknowledge | Acknowledge a pending return. No change to status.
[**ledgerContractsContractIdReturnsReturnIdCancelPost**](ReturnsApi.md#ledgerContractsContractIdReturnsReturnIdCancelPost) | **POST** /ledger/contracts/{contractId}/returns/{returnId}/cancel | Cancel a return in \&quot;proposed\&quot; or \&quot;pending\&quot; state. Original proposer only.
[**ledgerContractsContractIdReturnsReturnIdGet**](ReturnsApi.md#ledgerContractsContractIdReturnsReturnIdGet) | **GET** /ledger/contracts/{contractId}/returns/{returnId} | Read a return
[**ledgerContractsContractIdReturnsReturnIdPatch**](ReturnsApi.md#ledgerContractsContractIdReturnsReturnIdPatch) | **PATCH** /ledger/contracts/{contractId}/returns/{returnId} | Update unilateral fields on a return
[**ledgerContractsContractIdReturnsReturnIdVenuecancelPost**](ReturnsApi.md#ledgerContractsContractIdReturnsReturnIdVenuecancelPost) | **POST** /ledger/contracts/{contractId}/returns/{returnId}/venuecancel | Cancel a return in \&quot;proposed\&quot; or \&quot;pending\&quot; state. Original proposer only.
[**ledgerContractsContractIdVenuereturnsPost**](ReturnsApi.md#ledgerContractsContractIdVenuereturnsPost) | **POST** /ledger/contracts/{contractId}/venuereturns | For delegated venues to create a return atomically.
[**ledgerReturnsGet**](ReturnsApi.md#ledgerReturnsGet) | **GET** /ledger/returns | Read collection of returns
[**ledgerReturnsReturnIdGet**](ReturnsApi.md#ledgerReturnsReturnIdGet) | **GET** /ledger/returns/{returnId} | Read a return

<a name="ledgerContractsContractIdReturnsGet"></a>
# **ledgerContractsContractIdReturnsGet**
> Returns ledgerContractsContractIdReturnsGet(contractId)

Read collection of returns against contract specified by &#x27;contractId&#x27;

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    Returns result = apiInstance.ledgerContractsContractIdReturnsGet(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdReturnsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**Returns**](Returns.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReturnsPost"></a>
# **ledgerContractsContractIdReturnsPost**
> ledgerContractsContractIdReturnsPost(body, contractId)

Create a return

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
ReturnProposal body = new ReturnProposal(); // ReturnProposal | New return proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    apiInstance.ledgerContractsContractIdReturnsPost(body, contractId);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdReturnsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ReturnProposal**](ReturnProposal.md)| New return proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdReturnsReturnIdAcknowledgePost"></a>
# **ledgerContractsContractIdReturnsReturnIdAcknowledgePost**
> LedgerResponse ledgerContractsContractIdReturnsReturnIdAcknowledgePost(body, contractId, returnId)

Acknowledge a pending return. No change to status.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
ReturnAcknowledgement body = new ReturnAcknowledgement(); // ReturnAcknowledgement | Acknowledge return with positive/negative intent
String contractId = "contractId_example"; // String | The unique identifier of a contract
String returnId = "returnId_example"; // String | The unique identifier of a return
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReturnsReturnIdAcknowledgePost(body, contractId, returnId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdReturnsReturnIdAcknowledgePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ReturnAcknowledgement**](ReturnAcknowledgement.md)| Acknowledge return with positive/negative intent |
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **returnId** | [**String**](.md)| The unique identifier of a return |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdReturnsReturnIdCancelPost"></a>
# **ledgerContractsContractIdReturnsReturnIdCancelPost**
> LedgerResponse ledgerContractsContractIdReturnsReturnIdCancelPost(contractId, returnId)

Cancel a return in \&quot;proposed\&quot; or \&quot;pending\&quot; state. Original proposer only.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String returnId = "returnId_example"; // String | The unique identifier of a return
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReturnsReturnIdCancelPost(contractId, returnId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdReturnsReturnIdCancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **returnId** | [**String**](.md)| The unique identifier of a return |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReturnsReturnIdGet"></a>
# **ledgerContractsContractIdReturnsReturnIdGet**
> ModelReturn ledgerContractsContractIdReturnsReturnIdGet(contractId, returnId)

Read a return

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String returnId = "returnId_example"; // String | The unique identifier of a return
try {
    ModelReturn result = apiInstance.ledgerContractsContractIdReturnsReturnIdGet(contractId, returnId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdReturnsReturnIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **returnId** | [**String**](.md)| The unique identifier of a return |

### Return type

[**ModelReturn**](ModelReturn.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReturnsReturnIdPatch"></a>
# **ledgerContractsContractIdReturnsReturnIdPatch**
> LedgerResponse ledgerContractsContractIdReturnsReturnIdPatch(contractId, returnId, body)

Update unilateral fields on a return

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String returnId = "returnId_example"; // String | The unique identifier of a return
ReturnsReturnIdBody body = new ReturnsReturnIdBody(); // ReturnsReturnIdBody | 
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReturnsReturnIdPatch(contractId, returnId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdReturnsReturnIdPatch");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **returnId** | [**String**](.md)| The unique identifier of a return |
 **body** | [**ReturnsReturnIdBody**](ReturnsReturnIdBody.md)|  | [optional]

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdReturnsReturnIdVenuecancelPost"></a>
# **ledgerContractsContractIdReturnsReturnIdVenuecancelPost**
> LedgerResponse ledgerContractsContractIdReturnsReturnIdVenuecancelPost(contractId, returnId)

Cancel a return in \&quot;proposed\&quot; or \&quot;pending\&quot; state. Original proposer only.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String returnId = "returnId_example"; // String | The unique identifier of a return
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReturnsReturnIdVenuecancelPost(contractId, returnId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdReturnsReturnIdVenuecancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **returnId** | [**String**](.md)| The unique identifier of a return |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdVenuereturnsPost"></a>
# **ledgerContractsContractIdVenuereturnsPost**
> ledgerContractsContractIdVenuereturnsPost(body, contractId)

For delegated venues to create a return atomically.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
ReturnProposal body = new ReturnProposal(); // ReturnProposal | New return proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    apiInstance.ledgerContractsContractIdVenuereturnsPost(body, contractId);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerContractsContractIdVenuereturnsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ReturnProposal**](ReturnProposal.md)| New return proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerReturnsGet"></a>
# **ledgerReturnsGet**
> Returns ledgerReturnsGet()

Read collection of returns

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
try {
    Returns result = apiInstance.ledgerReturnsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerReturnsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Returns**](Returns.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerReturnsReturnIdGet"></a>
# **ledgerReturnsReturnIdGet**
> ModelReturn ledgerReturnsReturnIdGet(returnId)

Read a return

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReturnsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReturnsApi apiInstance = new ReturnsApi();
String returnId = "returnId_example"; // String | The unique identifier of a return
try {
    ModelReturn result = apiInstance.ledgerReturnsReturnIdGet(returnId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReturnsApi#ledgerReturnsReturnIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **returnId** | [**String**](.md)| The unique identifier of a return |

### Return type

[**ModelReturn**](ModelReturn.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

