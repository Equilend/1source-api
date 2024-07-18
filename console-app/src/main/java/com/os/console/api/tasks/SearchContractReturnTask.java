package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.ModelReturn;
import com.os.console.util.RESTUtil;

public class SearchContractReturnTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractReturnTask.class);

	private WebClient webClient;
	private Contract contract;
	private String returnId;

	private ModelReturn modelReturn;
	
	public SearchContractReturnTask(WebClient webClient, Contract contract, String returnId) {
		this.webClient = webClient;
		this.contract = contract;
		this.returnId = returnId;
	}

	@Override
	public void run() {

		modelReturn = (ModelReturn) RESTUtil.getRequest(webClient, "/contracts/" + contract.getContractId() + "/returns/" + returnId, ModelReturn.class);

		if (modelReturn == null) {
			logger.warn("Invalid return object or return not found");
			System.out.println("return not found");
		} else {
			System.out.println("complete");
			System.out.println();			
		}
	}

	public ModelReturn getReturn() {
		return modelReturn;
	}
	
	
}
