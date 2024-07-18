package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.ModelReturn;
import com.os.console.util.RESTUtil;

public class SearchReturnTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchReturnTask.class);

	private WebClient webClient;
	private String returnId;

	private ModelReturn modelReturn;
	
	public SearchReturnTask(WebClient webClient, String returnId) {
		this.webClient = webClient;
		this.returnId = returnId;
	}

	@Override
	public void run() {

		modelReturn = (ModelReturn) RESTUtil.getRequest(webClient, "/returns/" + returnId, ModelReturn.class);

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
