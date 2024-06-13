# ContractsApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerContractsContractIdApprovePost**](ContractsApi.md#ledgerContractsContractIdApprovePost) | **POST** /ledger/contracts/{contractId}/approve | Approve a contract in \&quot;proposed\&quot; state
[**ledgerContractsContractIdCancelPost**](ContractsApi.md#ledgerContractsContractIdCancelPost) | **POST** /ledger/contracts/{contractId}/cancel | Cancel a contract in \&quot;proposed\&quot; state. Original proposer only.
[**ledgerContractsContractIdCancelpendingPost**](ContractsApi.md#ledgerContractsContractIdCancelpendingPost) | **POST** /ledger/contracts/{contractId}/cancelpending | Cancel a contract in \&quot;pending\&quot; state. Either party can initiate.
[**ledgerContractsContractIdDeclinePost**](ContractsApi.md#ledgerContractsContractIdDeclinePost) | **POST** /ledger/contracts/{contractId}/decline | Decline a contract in \&quot;proposed\&quot; state
[**ledgerContractsContractIdGet**](ContractsApi.md#ledgerContractsContractIdGet) | **GET** /ledger/contracts/{contractId} | Read a specific contract the user is authorized to access
[**ledgerContractsContractIdHistoryGet**](ContractsApi.md#ledgerContractsContractIdHistoryGet) | **GET** /ledger/contracts/{contractId}/history | Return an ordered history of this contract. Each contract has a reference event that triggered a new version.
[**ledgerContractsContractIdPatch**](ContractsApi.md#ledgerContractsContractIdPatch) | **PATCH** /ledger/contracts/{contractId} | Update unilateral fields in a contract
[**ledgerContractsContractIdRatehistoryGet**](ContractsApi.md#ledgerContractsContractIdRatehistoryGet) | **GET** /ledger/contracts/{contractId}/ratehistory | Return an ordered history of effective rate changes.
[**ledgerContractsContractIdSplitContractSplitIdApprovePost**](ContractsApi.md#ledgerContractsContractIdSplitContractSplitIdApprovePost) | **POST** /ledger/contracts/{contractId}/split/{contractSplitId}/approve | Approve a contract split in \&quot;proposed\&quot; state.
[**ledgerContractsContractIdSplitContractSplitIdGet**](ContractsApi.md#ledgerContractsContractIdSplitContractSplitIdGet) | **GET** /ledger/contracts/{contractId}/split/{contractSplitId} | Retrieve a contract split.
[**ledgerContractsContractIdSplitPost**](ContractsApi.md#ledgerContractsContractIdSplitPost) | **POST** /ledger/contracts/{contractId}/split | Split an open contract into multiple lots
[**ledgerContractsGet**](ContractsApi.md#ledgerContractsGet) | **GET** /ledger/contracts | Read a collection of contracts. Defaults to return the last 100 contracts created.
[**ledgerContractsPost**](ContractsApi.md#ledgerContractsPost) | **POST** /ledger/contracts | Create a contract in \&quot;proposal\&quot; state. Normally done by the Lend side

<a name="ledgerContractsContractIdApprovePost"></a>
# **ledgerContractsContractIdApprovePost**
> LedgerResponse ledgerContractsContractIdApprovePost(body, contractId)

Approve a contract in \&quot;proposed\&quot; state

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
ContractProposalApproval body = new ContractProposalApproval(); // ContractProposalApproval | Update settlement instructions on an existing contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdApprovePost(body, contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdApprovePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ContractProposalApproval**](ContractProposalApproval.md)| Update settlement instructions on an existing contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdCancelPost"></a>
# **ledgerContractsContractIdCancelPost**
> LedgerResponse ledgerContractsContractIdCancelPost(contractId)

Cancel a contract in \&quot;proposed\&quot; state. Original proposer only.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdCancelPost(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdCancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdCancelpendingPost"></a>
# **ledgerContractsContractIdCancelpendingPost**
> LedgerResponse ledgerContractsContractIdCancelpendingPost(contractId)

Cancel a contract in \&quot;pending\&quot; state. Either party can initiate.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdCancelpendingPost(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdCancelpendingPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdDeclinePost"></a>
# **ledgerContractsContractIdDeclinePost**
> LedgerResponse ledgerContractsContractIdDeclinePost(contractId)

Decline a contract in \&quot;proposed\&quot; state

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdDeclinePost(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdDeclinePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdGet"></a>
# **ledgerContractsContractIdGet**
> Contract ledgerContractsContractIdGet(contractId)

Read a specific contract the user is authorized to access

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    Contract result = apiInstance.ledgerContractsContractIdGet(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**Contract**](Contract.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdHistoryGet"></a>
# **ledgerContractsContractIdHistoryGet**
> Contracts ledgerContractsContractIdHistoryGet(contractId, since, before, size)

Return an ordered history of this contract. Each contract has a reference event that triggered a new version.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
Date since = new Date(); // Date | Events (since) timestamp UTC
Date before = new Date(); // Date | Events (before) timestamp UTC
Integer size = 56; // Integer | Number of contract changes to be returned. Can be used to facilitate paging
try {
    Contracts result = apiInstance.ledgerContractsContractIdHistoryGet(contractId, since, before, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdHistoryGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **since** | **Date**| Events (since) timestamp UTC | [optional]
 **before** | **Date**| Events (before) timestamp UTC | [optional]
 **size** | **Integer**| Number of contract changes to be returned. Can be used to facilitate paging | [optional]

### Return type

[**Contracts**](Contracts.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdPatch"></a>
# **ledgerContractsContractIdPatch**
> LedgerResponse ledgerContractsContractIdPatch(contractId, body)

Update unilateral fields in a contract

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
ContractsContractIdBody body = new ContractsContractIdBody(); // ContractsContractIdBody | 
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdPatch(contractId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdPatch");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **body** | [**ContractsContractIdBody**](ContractsContractIdBody.md)|  | [optional]

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdRatehistoryGet"></a>
# **ledgerContractsContractIdRatehistoryGet**
> Rates ledgerContractsContractIdRatehistoryGet(contractId, since, before, size)

Return an ordered history of effective rate changes.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
Date since = new Date(); // Date | Events (since) timestamp UTC
Date before = new Date(); // Date | Events (before) timestamp UTC
Integer size = 56; // Integer | Number of effective rates to be returned. Can be used to facilitate paging
try {
    Rates result = apiInstance.ledgerContractsContractIdRatehistoryGet(contractId, since, before, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdRatehistoryGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **since** | **Date**| Events (since) timestamp UTC | [optional]
 **before** | **Date**| Events (before) timestamp UTC | [optional]
 **size** | **Integer**| Number of effective rates to be returned. Can be used to facilitate paging | [optional]

### Return type

[**Rates**](Rates.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdSplitContractSplitIdApprovePost"></a>
# **ledgerContractsContractIdSplitContractSplitIdApprovePost**
> LedgerResponse ledgerContractsContractIdSplitContractSplitIdApprovePost(body, contractId, contractSplitId)

Approve a contract split in \&quot;proposed\&quot; state.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
List<ContractSplitLotAppoval> body = Arrays.asList(new ContractSplitLotAppoval()); // List<ContractSplitLotAppoval> | Split proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
String contractSplitId = "contractSplitId_example"; // String | The unique identifier of a proposed contract split
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdSplitContractSplitIdApprovePost(body, contractId, contractSplitId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdSplitContractSplitIdApprovePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;ContractSplitLotAppoval&gt;**](ContractSplitLotAppoval.md)| Split proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **contractSplitId** | [**String**](.md)| The unique identifier of a proposed contract split |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdSplitContractSplitIdGet"></a>
# **ledgerContractsContractIdSplitContractSplitIdGet**
> ContractSplit ledgerContractsContractIdSplitContractSplitIdGet(contractId, contractSplitId)

Retrieve a contract split.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String contractSplitId = "contractSplitId_example"; // String | The unique identifier of a contract split
try {
    ContractSplit result = apiInstance.ledgerContractsContractIdSplitContractSplitIdGet(contractId, contractSplitId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdSplitContractSplitIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **contractSplitId** | [**String**](.md)| The unique identifier of a contract split |

### Return type

[**ContractSplit**](ContractSplit.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdSplitPost"></a>
# **ledgerContractsContractIdSplitPost**
> LedgerResponse ledgerContractsContractIdSplitPost(body, contractId)

Split an open contract into multiple lots

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
List<ContractSplitLot> body = Arrays.asList(new ContractSplitLot()); // List<ContractSplitLot> | Split proposed against contract
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdSplitPost(body, contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsContractIdSplitPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;ContractSplitLot&gt;**](ContractSplitLot.md)| Split proposed against contract |
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsGet"></a>
# **ledgerContractsGet**
> Contracts ledgerContractsGet(since, before, size, contractStatus, figi, sedol, cusip, ticker, isin, internalRefId, partyId)

Read a collection of contracts. Defaults to return the last 100 contracts created.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
Date since = new Date(); // Date | Contracts created (since) timestamp UTC
Date before = new Date(); // Date | Contracts created (before) timestamp UTC
Integer size = 56; // Integer | Number of contracts to be returned. Can be used to facilitate paging
ContractStatus contractStatus = new ContractStatus(); // ContractStatus | Contracts with status matching CONTRACT STATUS
String figi = "figi_example"; // String | Contracts with instrument matching FIGI
String sedol = "sedol_example"; // String | Contracts with instrument matching SEDOL
String cusip = "cusip_example"; // String | Contracts with instrument matching CUSIP
String ticker = "ticker_example"; // String | Contracts with instrument matching TICKER
String isin = "isin_example"; // String | Contracts with instrument matching ISIN
String internalRefId = "internalRefId_example"; // String | Contracts with internalRef matching INTERNAL REF ID
String partyId = "partyId_example"; // String | Contracts with a transacting party mathing PARTY ID
try {
    Contracts result = apiInstance.ledgerContractsGet(since, before, size, contractStatus, figi, sedol, cusip, ticker, isin, internalRefId, partyId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Contracts created (since) timestamp UTC | [optional]
 **before** | **Date**| Contracts created (before) timestamp UTC | [optional]
 **size** | **Integer**| Number of contracts to be returned. Can be used to facilitate paging | [optional]
 **contractStatus** | [**ContractStatus**](.md)| Contracts with status matching CONTRACT STATUS | [optional]
 **figi** | **String**| Contracts with instrument matching FIGI | [optional]
 **sedol** | **String**| Contracts with instrument matching SEDOL | [optional]
 **cusip** | **String**| Contracts with instrument matching CUSIP | [optional]
 **ticker** | **String**| Contracts with instrument matching TICKER | [optional]
 **isin** | **String**| Contracts with instrument matching ISIN | [optional]
 **internalRefId** | **String**| Contracts with internalRef matching INTERNAL REF ID | [optional]
 **partyId** | [**String**](.md)| Contracts with a transacting party mathing PARTY ID | [optional]

### Return type

[**Contracts**](Contracts.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsPost"></a>
# **ledgerContractsPost**
> LedgerResponse ledgerContractsPost(body)

Create a contract in \&quot;proposal\&quot; state. Normally done by the Lend side

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.ContractsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

ContractsApi apiInstance = new ContractsApi();
ContractProposal body = new ContractProposal(); // ContractProposal | New contract proposed for inclusion in the ledger
try {
    LedgerResponse result = apiInstance.ledgerContractsPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContractsApi#ledgerContractsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ContractProposal**](ContractProposal.md)| New contract proposed for inclusion in the ledger |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

