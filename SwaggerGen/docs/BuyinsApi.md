# BuyinsApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerBuyinsCompletesBuyinCompleteIdGet**](BuyinsApi.md#ledgerBuyinsCompletesBuyinCompleteIdGet) | **GET** /ledger/buyins/completes/{buyinCompleteId} | Read a completed buyin
[**ledgerBuyinsCompletesGet**](BuyinsApi.md#ledgerBuyinsCompletesGet) | **GET** /ledger/buyins/completes | Read collection of completed buyins
[**ledgerBuyinsInitiatesBuyinInitiateIdGet**](BuyinsApi.md#ledgerBuyinsInitiatesBuyinInitiateIdGet) | **GET** /ledger/buyins/initiates/{buyinInitiateId} | Read an initiated buyin
[**ledgerBuyinsInitiatesGet**](BuyinsApi.md#ledgerBuyinsInitiatesGet) | **GET** /ledger/buyins/initiates | Read collection of initiated buyins
[**ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdAcceptPost**](BuyinsApi.md#ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdAcceptPost) | **POST** /ledger/contracts/{contractId}/buyins/completes/{buyinCompleteId}/accept | Complete some or all of an initiated Buyin
[**ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdGet**](BuyinsApi.md#ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdGet) | **GET** /ledger/contracts/{contractId}/buyins/completes/{buyinCompleteId} | Read a completed Buyin
[**ledgerContractsContractIdBuyinsCompletesGet**](BuyinsApi.md#ledgerContractsContractIdBuyinsCompletesGet) | **GET** /ledger/contracts/{contractId}/buyins/completes | Read collection of completed buyins against contract specified by &#x27;contractId&#x27;
[**ledgerContractsContractIdBuyinsCompletesPost**](BuyinsApi.md#ledgerContractsContractIdBuyinsCompletesPost) | **POST** /ledger/contracts/{contractId}/buyins/completes | Complete a buyin
[**ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdCancelPost**](BuyinsApi.md#ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdCancelPost) | **POST** /ledger/contracts/{contractId}/buyins/initiates/{buyinInitiateId}/cancel | Cancel an initiated buyin. Original proposer only.
[**ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdGet**](BuyinsApi.md#ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdGet) | **GET** /ledger/contracts/{contractId}/buyins/initiates/{buyinInitiateId} | Read an initiated buyin
[**ledgerContractsContractIdBuyinsInitiatesGet**](BuyinsApi.md#ledgerContractsContractIdBuyinsInitiatesGet) | **GET** /ledger/contracts/{contractId}/buyins/initiates | Read collection of initiated buyins against contract specified by &#x27;contractId&#x27;
[**ledgerContractsContractIdBuyinsInitiatesPost**](BuyinsApi.md#ledgerContractsContractIdBuyinsInitiatesPost) | **POST** /ledger/contracts/{contractId}/buyins/initiates | Initiate a buyin

<a name="ledgerBuyinsCompletesBuyinCompleteIdGet"></a>
# **ledgerBuyinsCompletesBuyinCompleteIdGet**
> BuyinComplete ledgerBuyinsCompletesBuyinCompleteIdGet(buyinCompleteId)

Read a completed buyin

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String buyinCompleteId = "buyinCompleteId_example"; // String | The unique identifier of a completed buyin
try {
    BuyinComplete result = apiInstance.ledgerBuyinsCompletesBuyinCompleteIdGet(buyinCompleteId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerBuyinsCompletesBuyinCompleteIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **buyinCompleteId** | [**String**](.md)| The unique identifier of a completed buyin |

### Return type

[**BuyinComplete**](BuyinComplete.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerBuyinsCompletesGet"></a>
# **ledgerBuyinsCompletesGet**
> BuyinCompletes ledgerBuyinsCompletesGet()

Read collection of completed buyins

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
try {
    BuyinCompletes result = apiInstance.ledgerBuyinsCompletesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerBuyinsCompletesGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**BuyinCompletes**](BuyinCompletes.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerBuyinsInitiatesBuyinInitiateIdGet"></a>
# **ledgerBuyinsInitiatesBuyinInitiateIdGet**
> BuyinInitiate ledgerBuyinsInitiatesBuyinInitiateIdGet(buyinInitiateId)

Read an initiated buyin

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String buyinInitiateId = "buyinInitiateId_example"; // String | The unique identifier of an initiated buyin
try {
    BuyinInitiate result = apiInstance.ledgerBuyinsInitiatesBuyinInitiateIdGet(buyinInitiateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerBuyinsInitiatesBuyinInitiateIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **buyinInitiateId** | [**String**](.md)| The unique identifier of an initiated buyin |

### Return type

[**BuyinInitiate**](BuyinInitiate.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerBuyinsInitiatesGet"></a>
# **ledgerBuyinsInitiatesGet**
> BuyinInitiates ledgerBuyinsInitiatesGet()

Read collection of initiated buyins

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
try {
    BuyinInitiates result = apiInstance.ledgerBuyinsInitiatesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerBuyinsInitiatesGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**BuyinInitiates**](BuyinInitiates.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdAcceptPost"></a>
# **ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdAcceptPost**
> LedgerResponse ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdAcceptPost(contractId, buyinCompleteId)

Complete some or all of an initiated Buyin

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String buyinCompleteId = "buyinCompleteId_example"; // String | The unique identifier of a completed buyin
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdAcceptPost(contractId, buyinCompleteId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdAcceptPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **buyinCompleteId** | [**String**](.md)| The unique identifier of a completed buyin |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdGet"></a>
# **ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdGet**
> BuyinComplete ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdGet(contractId, buyinCompleteId)

Read a completed Buyin

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String buyinCompleteId = "buyinCompleteId_example"; // String | The unique identifier of a completed buyin
try {
    BuyinComplete result = apiInstance.ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdGet(contractId, buyinCompleteId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsCompletesBuyinCompleteIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **buyinCompleteId** | [**String**](.md)| The unique identifier of a completed buyin |

### Return type

[**BuyinComplete**](BuyinComplete.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsCompletesGet"></a>
# **ledgerContractsContractIdBuyinsCompletesGet**
> BuyinCompletes ledgerContractsContractIdBuyinsCompletesGet(contractId)

Read collection of completed buyins against contract specified by &#x27;contractId&#x27;

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    BuyinCompletes result = apiInstance.ledgerContractsContractIdBuyinsCompletesGet(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsCompletesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**BuyinCompletes**](BuyinCompletes.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsCompletesPost"></a>
# **ledgerContractsContractIdBuyinsCompletesPost**
> ledgerContractsContractIdBuyinsCompletesPost(contractId, body)

Complete a buyin

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
BuyinCompleteRequest body = new BuyinCompleteRequest(); // BuyinCompleteRequest | 
try {
    apiInstance.ledgerContractsContractIdBuyinsCompletesPost(contractId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsCompletesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **body** | [**BuyinCompleteRequest**](BuyinCompleteRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdCancelPost"></a>
# **ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdCancelPost**
> LedgerResponse ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdCancelPost(contractId, buyinInitiateId)

Cancel an initiated buyin. Original proposer only.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String buyinInitiateId = "buyinInitiateId_example"; // String | The unique identifier of an initiated buyin
try {
    LedgerResponse result = apiInstance.ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdCancelPost(contractId, buyinInitiateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdCancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **buyinInitiateId** | [**String**](.md)| The unique identifier of an initiated buyin |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdGet"></a>
# **ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdGet**
> BuyinInitiate ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdGet(contractId, buyinInitiateId)

Read an initiated buyin

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String buyinInitiateId = "buyinInitiateId_example"; // String | The unique identifier of an initiated buyin
try {
    BuyinInitiate result = apiInstance.ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdGet(contractId, buyinInitiateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsInitiatesBuyinInitiateIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **buyinInitiateId** | [**String**](.md)| The unique identifier of an initiated buyin |

### Return type

[**BuyinInitiate**](BuyinInitiate.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsInitiatesGet"></a>
# **ledgerContractsContractIdBuyinsInitiatesGet**
> BuyinInitiates ledgerContractsContractIdBuyinsInitiatesGet(contractId)

Read collection of initiated buyins against contract specified by &#x27;contractId&#x27;

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
try {
    BuyinInitiates result = apiInstance.ledgerContractsContractIdBuyinsInitiatesGet(contractId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsInitiatesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |

### Return type

[**BuyinInitiates**](BuyinInitiates.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdBuyinsInitiatesPost"></a>
# **ledgerContractsContractIdBuyinsInitiatesPost**
> ledgerContractsContractIdBuyinsInitiatesPost(contractId, body)

Initiate a buyin

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.BuyinsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

BuyinsApi apiInstance = new BuyinsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
BuyinInitiateRequest body = new BuyinInitiateRequest(); // BuyinInitiateRequest | 
try {
    apiInstance.ledgerContractsContractIdBuyinsInitiatesPost(contractId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling BuyinsApi#ledgerContractsContractIdBuyinsInitiatesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **body** | [**BuyinInitiateRequest**](BuyinInitiateRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

