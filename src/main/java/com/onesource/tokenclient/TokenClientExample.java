package com.onesource.tokenclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class TokenClientExample {

	public static void main(String[] args) throws Exception {
		httpPostRequest();
	}

	public static void httpPostRequest() throws URISyntaxException, IOException, InterruptedException {

		HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

		Map<String, String> formData = new HashMap<>();
		formData.put("client_id", "IntegrationTestClient");
		formData.put("client_secret", "a14ca593-81e9-4a44-9e0d-b54994250bdc");
		formData.put("grant_type", "password");
		formData.put("username", "user1");
		formData.put("password", "1234XYZ");

		HttpRequest request = HttpRequest
				.newBuilder(new URI("https://stageauth.equilend.com/auth/realms/1Source/protocol/openid-connect/token"))
				.version(HttpClient.Version.HTTP_2)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(BodyPublishers.ofString(getFormDataAsString(formData)))
				.build();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		
		JSONObject obj = new JSONObject(response.body());
		
		System.out.println(obj.getString("access_token"));
	}

	private static String getFormDataAsString(Map<String, String> formData) {
		StringBuilder formBodyBuilder = new StringBuilder();
		for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
			if (formBodyBuilder.length() > 0) {
				formBodyBuilder.append("&");
			}
			formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
			formBodyBuilder.append("=");
			formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
		}
		return formBodyBuilder.toString();
	}
}
