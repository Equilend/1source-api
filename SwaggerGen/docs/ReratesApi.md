# ReratesApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerContractsContractIdReratesGet**](ReratesApi.md#ledgerContractsContractIdReratesGet) | **GET** /ledger/contracts/{contractId}/rerates | Read collection of rerates against contract specified by &#x27;contractId&#x27;
[**ledgerContractsContractIdReratesPost**](ReratesApi.md#ledgerContractsContractIdReratesPost) | **POST** /ledger/contracts/{contractId}/rerates | Create a rerate.
[**ledgerContractsContractIdReratesRerateIdApprovePost**](ReratesApi.md#ledgerContractsContractIdReratesRerateIdApprovePost) | **POST** /ledger/contracts/{contractId}/rerates/{rerateId}/approve | Approve a rerate in \&quot;proposed\&quot; state.
[**ledgerContractsContractIdReratesRerateIdCancelPost**](ReratesApi.md#ledgerContractsContractIdReratesRerateIdCancelPost) | **POST** /ledger/contracts/{contractId}/rerates/{rerateId}/cancel | Cancel a rerate in \&quot;proposed\&quot; state. Original proposer only.
[**ledgerContractsContractIdReratesRerateIdCancelpendingPost**](ReratesApi.md#ledgerContractsContractIdReratesRerateIdCancelpendingPost) | **POST** /ledger/contracts/{contractId}/rerates/{rerateId}/cancelpending | Cancel a rerate in \&quot;pending\&quot; state. Either party can initiate. If canceled by a delegated Venue, the cancel will be done unilaterally
[**ledgerContractsContractIdReratesRerateIdDeclinePost**](ReratesApi.md#ledgerContractsContractIdReratesRerateIdDeclinePost) | **POST** /ledger/contracts/{contractId}/rerates/{rerateId}/decline | Decline a rerate in \&quot;proposed\&quot; state.
[**ledgerContractsContractIdReratesRerateIdGet**](ReratesApi.md#ledgerContractsContractIdReratesRerateIdGet) | **GET** /ledger/contracts/{contractId}/rerates/{rerateId} | Read a rerate
[**ledgerContractsContractIdReratesRerateIdVenuecancelpendingPost**](ReratesApi.md#ledgerContractsContractIdReratesRerateIdVenuecancelpendingPost) | **POST** /ledger/contracts/{contractId}/rerates/{rerateId}/venuecancelpending | Cancel a rerate in \&quot;pending\&quot; state. Either party can initiate. If canceled by a delegated Venue, the cancel will be done unilaterally
[**ledgerContractsContractIdVenuereratesPost**](ReratesApi.md#ledgerContractsContractIdVenuereratesPost) | **POST** /ledger/contracts/{contractId}/venuererates | For delegated venues to create a rerate atomically.
[**ledgerReratesGet**](ReratesApi.md#ledgerReratesGet) | **GET** /ledger/rerates | Read collection of rerates
[**ledgerReratesRerateIdGet**](ReratesApi.md#ledgerReratesRerateIdGet) | **GET** /ledger/rerates/{rerateId} | Read a rerate

<a name="ledgerContractsContractIdReratesGet"></a>
# **ledgerContractsContractIdReratesGet**
> Rerates ledgerContractsContractIdReratesGet(contractId)

Read collection of rerates against contract specified by &#x27;contractId&#x27;

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    Rerates result = apiInstance.ledgerContractsContractIdReratesGet(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**Rerates**](Rerates.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReratesPost"></a>
# **ledgerContractsContractIdReratesPost**
> ledgerContractsContractIdReratesPost(body, contractId)

Create a rerate.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
RerateProposal body = new RerateProposal(); // RerateProposal | New rerate proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    apiInstance.ledgerContractsContractIdReratesPost(body, contractId);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RerateProposal**](RerateProposal.md)| New rerate proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdReratesRerateIdApprovePost"></a>
# **ledgerContractsContractIdReratesRerateIdApprovePost**
> LedgerResponse ledgerContractsContractIdReratesRerateIdApprovePost(contractId, rerateId)

Approve a rerate in \&quot;proposed\&quot; state.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String rerateId = "rerateId_example"; // String | The unique identifier of a rerate
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReratesRerateIdApprovePost(contractId, rerateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesRerateIdApprovePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **rerateId** | [**String**](.md)| The unique identifier of a rerate |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReratesRerateIdCancelPost"></a>
# **ledgerContractsContractIdReratesRerateIdCancelPost**
> LedgerResponse ledgerContractsContractIdReratesRerateIdCancelPost(contractId, rerateId)

Cancel a rerate in \&quot;proposed\&quot; state. Original proposer only.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String rerateId = "rerateId_example"; // String | The unique identifier of a rerate
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReratesRerateIdCancelPost(contractId, rerateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesRerateIdCancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **rerateId** | [**String**](.md)| The unique identifier of a rerate |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReratesRerateIdCancelpendingPost"></a>
# **ledgerContractsContractIdReratesRerateIdCancelpendingPost**
> LedgerResponse ledgerContractsContractIdReratesRerateIdCancelpendingPost(contractId, rerateId)

Cancel a rerate in \&quot;pending\&quot; state. Either party can initiate. If canceled by a delegated Venue, the cancel will be done unilaterally

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String rerateId = "rerateId_example"; // String | The unique identifier of a rerate
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReratesRerateIdCancelpendingPost(contractId, rerateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesRerateIdCancelpendingPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **rerateId** | [**String**](.md)| The unique identifier of a rerate |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReratesRerateIdDeclinePost"></a>
# **ledgerContractsContractIdReratesRerateIdDeclinePost**
> LedgerResponse ledgerContractsContractIdReratesRerateIdDeclinePost(contractId, rerateId)

Decline a rerate in \&quot;proposed\&quot; state.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String rerateId = "rerateId_example"; // String | The unique identifier of a rerate
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReratesRerateIdDeclinePost(contractId, rerateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesRerateIdDeclinePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **rerateId** | [**String**](.md)| The unique identifier of a rerate |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReratesRerateIdGet"></a>
# **ledgerContractsContractIdReratesRerateIdGet**
> Rerate ledgerContractsContractIdReratesRerateIdGet(contractId, rerateId)

Read a rerate

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String rerateId = "rerateId_example"; // String | The unique identifier of a rerate
try {
    Rerate result = apiInstance.ledgerContractsContractIdReratesRerateIdGet(contractId, rerateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesRerateIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **rerateId** | [**String**](.md)| The unique identifier of a rerate |

### Return type

[**Rerate**](Rerate.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdReratesRerateIdVenuecancelpendingPost"></a>
# **ledgerContractsContractIdReratesRerateIdVenuecancelpendingPost**
> LedgerResponse ledgerContractsContractIdReratesRerateIdVenuecancelpendingPost(contractId, rerateId)

Cancel a rerate in \&quot;pending\&quot; state. Either party can initiate. If canceled by a delegated Venue, the cancel will be done unilaterally

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String rerateId = "rerateId_example"; // String | The unique identifier of a rerate
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdReratesRerateIdVenuecancelpendingPost(contractId, rerateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdReratesRerateIdVenuecancelpendingPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **rerateId** | [**String**](.md)| The unique identifier of a rerate |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdVenuereratesPost"></a>
# **ledgerContractsContractIdVenuereratesPost**
> ledgerContractsContractIdVenuereratesPost(body, contractId)

For delegated venues to create a rerate atomically.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
RerateProposal body = new RerateProposal(); // RerateProposal | New rerate proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    apiInstance.ledgerContractsContractIdVenuereratesPost(body, contractId);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerContractsContractIdVenuereratesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RerateProposal**](RerateProposal.md)| New rerate proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerReratesGet"></a>
# **ledgerReratesGet**
> Rerates ledgerReratesGet(since, before, size, rerateStatus, figi, sedol, cusip, ticker, isin, partyId)

Read collection of rerates

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
Date since = new Date(); // Date | Rerates created (since) timestamp UTC
Date before = new Date(); // Date | Rerates created (before) timestamp UTC
Integer size = 56; // Integer | Number of rerates to be returned. Can be used to facilitate paging
RerateStatus rerateStatus = new RerateStatus(); // RerateStatus | Contracts matching status RERATE STATUS
String figi = "figi_example"; // String | Rerates with instrument matching FIGI
String sedol = "sedol_example"; // String | Rerates with instrument matching SEDOL
String cusip = "cusip_example"; // String | Rerates with instrument matching CUSIP
String ticker = "ticker_example"; // String | Rerates with instrument matching TICKER
String isin = "isin_example"; // String | Rerates with instrument matching ISIN
String partyId = "partyId_example"; // String | Rerates with a transacting party mathing PARTY ID
try {
    Rerates result = apiInstance.ledgerReratesGet(since, before, size, rerateStatus, figi, sedol, cusip, ticker, isin, partyId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerReratesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Rerates created (since) timestamp UTC | [optional]
 **before** | **Date**| Rerates created (before) timestamp UTC | [optional]
 **size** | **Integer**| Number of rerates to be returned. Can be used to facilitate paging | [optional]
 **rerateStatus** | [**RerateStatus**](.md)| Contracts matching status RERATE STATUS | [optional]
 **figi** | **String**| Rerates with instrument matching FIGI | [optional]
 **sedol** | **String**| Rerates with instrument matching SEDOL | [optional]
 **cusip** | **String**| Rerates with instrument matching CUSIP | [optional]
 **ticker** | **String**| Rerates with instrument matching TICKER | [optional]
 **isin** | **String**| Rerates with instrument matching ISIN | [optional]
 **partyId** | [**String**](.md)| Rerates with a transacting party mathing PARTY ID | [optional]

### Return type

[**Rerates**](Rerates.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerReratesRerateIdGet"></a>
# **ledgerReratesRerateIdGet**
> Rerate ledgerReratesRerateIdGet(rerateId)

Read a rerate

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ReratesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ReratesApi apiInstance = new ReratesApi();
String rerateId = "rerateId_example"; // String | The unique identifier of a rerate
try {
    Rerate result = apiInstance.ledgerReratesRerateIdGet(rerateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReratesApi#ledgerReratesRerateIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **rerateId** | [**String**](.md)| The unique identifier of a rerate |

### Return type

[**Rerate**](Rerate.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

