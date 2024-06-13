# AgreementsApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerAgreementsAgreementIdGet**](AgreementsApi.md#ledgerAgreementsAgreementIdGet) | **GET** /ledger/agreements/{agreementId} | Read an agreement
[**ledgerAgreementsGet**](AgreementsApi.md#ledgerAgreementsGet) | **GET** /ledger/agreements | Read a collection of trade agreements. Defaults to return the last 100 trade agreeements created.
[**ledgerAgreementsPost**](AgreementsApi.md#ledgerAgreementsPost) | **POST** /ledger/agreements | Create a trade agreement
[**ledgerVenueAgreementsPost**](AgreementsApi.md#ledgerVenueAgreementsPost) | **POST** /ledger/venue/agreements | For delegated venues to create a trade agreement atomically

<a name="ledgerAgreementsAgreementIdGet"></a>
# **ledgerAgreementsAgreementIdGet**
> Agreement ledgerAgreementsAgreementIdGet(agreementId)

Read an agreement

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.AgreementsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

AgreementsApi apiInstance = new AgreementsApi();
String agreementId = "agreementId_example"; // String | The unique identifier of a trade agreement
try {
    Agreement result = apiInstance.ledgerAgreementsAgreementIdGet(agreementId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AgreementsApi#ledgerAgreementsAgreementIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **agreementId** | [**String**](.md)| The unique identifier of a trade agreement |

### Return type

[**Agreement**](Agreement.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerAgreementsGet"></a>
# **ledgerAgreementsGet**
> Agreements ledgerAgreementsGet(since, before, size)

Read a collection of trade agreements. Defaults to return the last 100 trade agreeements created.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.AgreementsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

AgreementsApi apiInstance = new AgreementsApi();
Date since = new Date(); // Date | Agreements created (since) timestamp UTC
Date before = new Date(); // Date | Agreements created (before) timestamp UTC
Integer size = 56; // Integer | Number of agreements to be returned. Can be used to facilitate paging
try {
    Agreements result = apiInstance.ledgerAgreementsGet(since, before, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AgreementsApi#ledgerAgreementsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Agreements created (since) timestamp UTC | [optional]
 **before** | **Date**| Agreements created (before) timestamp UTC | [optional]
 **size** | **Integer**| Number of agreements to be returned. Can be used to facilitate paging | [optional]

### Return type

[**Agreements**](Agreements.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerAgreementsPost"></a>
# **ledgerAgreementsPost**
> LedgerResponse ledgerAgreementsPost(body)

Create a trade agreement

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.AgreementsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

AgreementsApi apiInstance = new AgreementsApi();
TradeAgreement body = new TradeAgreement(); // TradeAgreement | Agreement from a execution venue introduced to the ledger
try {
    LedgerResponse result = apiInstance.ledgerAgreementsPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AgreementsApi#ledgerAgreementsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**TradeAgreement**](TradeAgreement.md)| Agreement from a execution venue introduced to the ledger |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ledgerVenueAgreementsPost"></a>
# **ledgerVenueAgreementsPost**
> LedgerResponse ledgerVenueAgreementsPost(body)

For delegated venues to create a trade agreement atomically

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.AgreementsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

AgreementsApi apiInstance = new AgreementsApi();
TradeAgreement body = new TradeAgreement(); // TradeAgreement | Agreement from a execution venue introduced to the ledger
try {
    LedgerResponse result = apiInstance.ledgerVenueAgreementsPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AgreementsApi#ledgerVenueAgreementsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**TradeAgreement**](TradeAgreement.md)| Agreement from a execution venue introduced to the ledger |

### Return type

[**LedgerResponse**](LedgerResponse.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

