# PartiesApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerPartiesGet**](PartiesApi.md#ledgerPartiesGet) | **GET** /ledger/parties | Read a collection of registered trading parties.

<a name="ledgerPartiesGet"></a>
# **ledgerPartiesGet**
> Parties ledgerPartiesGet(partyId, partyName, gleifLei, internalPartyId)

Read a collection of registered trading parties.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.PartiesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

PartiesApi apiInstance = new PartiesApi();
String partyId = "partyId_example"; // String | The ledger Party ID sometimes referred to as Short Code
String partyName = "partyName_example"; // String | Party name as defined during onboarding
String gleifLei = "gleifLei_example"; // String | Global Legal Entity Identifier Foundation - Legal Entity Identifier
String internalPartyId = "internalPartyId_example"; // String | Client internal or proprietary id for this party
try {
    Parties result = apiInstance.ledgerPartiesGet(partyId, partyName, gleifLei, internalPartyId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PartiesApi#ledgerPartiesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **partyId** | [**String**](.md)| The ledger Party ID sometimes referred to as Short Code | [optional]
 **partyName** | **String**| Party name as defined during onboarding | [optional]
 **gleifLei** | **String**| Global Legal Entity Identifier Foundation - Legal Entity Identifier | [optional]
 **internalPartyId** | **String**| Client internal or proprietary id for this party | [optional]

### Return type

[**Parties**](Parties.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

