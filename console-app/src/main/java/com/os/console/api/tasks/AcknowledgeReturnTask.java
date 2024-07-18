package com.os.console.api.tasks;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.ModelReturn;
import com.os.client.model.ReturnAcknowledgement;
import com.os.console.util.RESTUtil;

public class AcknowledgeReturnTask implements Runnable {

	private WebClient webClient;
	private ModelReturn modelReturn;
	private ReturnAcknowledgement returnAcknowledgement;

	public AcknowledgeReturnTask(WebClient webClient, ModelReturn modelReturn, ReturnAcknowledgement returnAcknowledgement) {
		this.webClient = webClient;
		this.modelReturn = modelReturn;
		this.returnAcknowledgement = returnAcknowledgement;
	}

	@Override
	public void run() {
		RESTUtil.postRequest(webClient, "/contracts/" + modelReturn.getContractId() + "/returns/" + modelReturn.getReturnId() + "/acknowledge", returnAcknowledgement);
	}
}
