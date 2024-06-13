# EventsApi

All URIs are relative to *https://stageapi.equilend.com/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ledgerContractsContractIdEventsEventIdGet**](EventsApi.md#ledgerContractsContractIdEventsEventIdGet) | **GET** /ledger/contracts/{contractId}/events/{eventId} | Read an event
[**ledgerContractsContractIdEventsGet**](EventsApi.md#ledgerContractsContractIdEventsGet) | **GET** /ledger/contracts/{contractId}/events | Read collection of events against a specific contract. With no parameters returns events since start of current day.
[**ledgerEventsEventIdGet**](EventsApi.md#ledgerEventsEventIdGet) | **GET** /ledger/events/{eventId} | Read an event
[**ledgerEventsGet**](EventsApi.md#ledgerEventsGet) | **GET** /ledger/events | Read collection of events. With no parameters returns events since start of current day.

<a name="ledgerContractsContractIdEventsEventIdGet"></a>
# **ledgerContractsContractIdEventsEventIdGet**
> Event ledgerContractsContractIdEventsEventIdGet(contractId, eventId)

Read an event

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.EventsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

EventsApi apiInstance = new EventsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
String eventId = "eventId_example"; // String | The unique identifier of an Event
try {
    Event result = apiInstance.ledgerContractsContractIdEventsEventIdGet(contractId, eventId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#ledgerContractsContractIdEventsEventIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **eventId** | [**String**](.md)| The unique identifier of an Event |

### Return type

[**Event**](Event.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerContractsContractIdEventsGet"></a>
# **ledgerContractsContractIdEventsGet**
> Events ledgerContractsContractIdEventsGet(contractId, eventType, fromEventId, since, before, size)

Read collection of events against a specific contract. With no parameters returns events since start of current day.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.EventsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

EventsApi apiInstance = new EventsApi();
String contractId = "contractId_example"; // String | The unique identifier of a contract
List<EventType> eventType = Arrays.asList(new EventType()); // List<EventType> | Filter by event type
Integer fromEventId = 56; // Integer | 
Date since = new Date(); // Date | Events (since) timestamp UTC
Date before = new Date(); // Date | Events (before) timestamp UTC
Integer size = 56; // Integer | Number of events to be returned. Can be used to facilitate paging
try {
    Events result = apiInstance.ledgerContractsContractIdEventsGet(contractId, eventType, fromEventId, since, before, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#ledgerContractsContractIdEventsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **contractId** | [**String**](.md)| The unique identifier of a contract |
 **eventType** | [**List&lt;EventType&gt;**](EventType.md)| Filter by event type | [optional]
 **fromEventId** | **Integer**|  | [optional]
 **since** | **Date**| Events (since) timestamp UTC | [optional]
 **before** | **Date**| Events (before) timestamp UTC | [optional]
 **size** | **Integer**| Number of events to be returned. Can be used to facilitate paging | [optional]

### Return type

[**Events**](Events.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerEventsEventIdGet"></a>
# **ledgerEventsEventIdGet**
> Event ledgerEventsEventIdGet(eventId)

Read an event

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.EventsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

EventsApi apiInstance = new EventsApi();
String eventId = "eventId_example"; // String | The unique identifier of an event
try {
    Event result = apiInstance.ledgerEventsEventIdGet(eventId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#ledgerEventsEventIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **eventId** | [**String**](.md)| The unique identifier of an event |

### Return type

[**Event**](Event.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="ledgerEventsGet"></a>
# **ledgerEventsGet**
> Events ledgerEventsGet(eventType, fromEventId, since, before, size)

Read collection of events. With no parameters returns events since start of current day.

### Example
```java
// Import classes:
//import com.os.client.invoker.ApiClient;
//import com.os.client.invoker.ApiException;
//import com.os.client.invoker.Configuration;
//import com.os.client.invoker.auth.*;
//import com.os.client.api.EventsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: stage_auth
OAuth stage_auth = (OAuth) defaultClient.getAuthentication("stage_auth");
stage_auth.setAccessToken("YOUR ACCESS TOKEN");

EventsApi apiInstance = new EventsApi();
List<EventType> eventType = Arrays.asList(new EventType()); // List<EventType> | Filter by event type
Integer fromEventId = 56; // Integer | 
Date since = new Date(); // Date | Events (since) timestamp UTC
Date before = new Date(); // Date | Events (before) timestamp UTC
Integer size = 56; // Integer | Number of events to be returned. Can be used to facilitate paging. Defaults to 100
try {
    Events result = apiInstance.ledgerEventsGet(eventType, fromEventId, since, before, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#ledgerEventsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **eventType** | [**List&lt;EventType&gt;**](EventType.md)| Filter by event type | [optional]
 **fromEventId** | **Integer**|  | [optional]
 **since** | **Date**| Events (since) timestamp UTC | [optional]
 **before** | **Date**| Events (before) timestamp UTC | [optional]
 **size** | **Integer**| Number of events to be returned. Can be used to facilitate paging. Defaults to 100 | [optional]

### Return type

[**Events**](Events.md)

### Authorization

[stage_auth](../README.md#stage_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

