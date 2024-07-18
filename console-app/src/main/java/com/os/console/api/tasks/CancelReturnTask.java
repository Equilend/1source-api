package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.ModelReturn;
import com.os.console.util.RESTUtil;

public class CancelReturnTask implements Runnable {

	private WebClient webClient;
	private ModelReturn modelReturn;

	public CancelReturnTask(WebClient webClient, ModelReturn modelReturn) {
		this.webClient = webClient;
		this.modelReturn = modelReturn;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/contracts/" + modelReturn.getContractId() + "/returns/" + modelReturn.getReturnId() + "/cancel");
	}
}
