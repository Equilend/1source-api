package com.onesource.demo.util;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    public RequestLoggingInterceptor() {
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        System.out.println("Request URI: " +request.getMethod() + " " + request.getURI());
//        System.out.println("Request Headers: " + request.getHeaders());
        System.out.println("Request Body: " + new String(body, StandardCharsets.UTF_8));

        return execution.execute(request, body);
    }

}
