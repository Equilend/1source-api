package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Contract;
import com.os.client.model.Recall;
import com.os.client.model.Recalls;
import com.os.console.api.ConsoleConfig;
import com.os.console.util.ConsoleOutputUtil;

import reactor.core.publisher.Mono;

public class SearchContractRecallsTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchContractRecallsTask.class);

	private WebClient webClient;
	private Contract contract;
	
	public SearchContractRecallsTask(WebClient webClient, Contract contract) {
		this.webClient = webClient;
		this.contract = contract;
	}

	@Override
	public void run() {
		
		Recalls recalls = webClient.get().uri("/contracts/" + contract.getContractId() + "/recalls")
				.headers(h -> h.setBearerAuth(ConsoleConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Recalls.class).block();

		if (recalls == null || recalls.size() == 0) {
			logger.warn("Invalid recalls object or no recalls");			
			System.out.println("no recalls found");
			printHeader();
		} else {
			System.out.println("complete");
			printHeader();
			int rows = 1;
			for (Recall recall : recalls) {
				if (rows % 15 == 0) {
					printHeader();
				}
				System.out.print(ConsoleOutputUtil.padSpaces(recall.getRecallId(), 40));
				System.out.print(ConsoleOutputUtil.padSpaces(recall.getStatus().toString(), 12));
				System.out.print(ConsoleOutputUtil.padSpaces(recall.getLastUpdateDatetime(), 30));
				System.out.print(ConsoleOutputUtil.padSpaces(recall.getRecallDate(), 15));
				System.out.print(ConsoleOutputUtil.padSpaces(recall.getRecallDueDate(), 18));
				System.out.print(ConsoleOutputUtil.padSpaces(recall.getQuantity(), 15));
				System.out.println();
				
				rows++;
			}
		}
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Recall Id", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("Status", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Last Update DateTime", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Recall Date", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("Recall Due Date", 18));
		System.out.print(ConsoleOutputUtil.padSpaces("Quantity", 15));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("---------", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("--------------------", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("-----------", 15));
		System.out.print(ConsoleOutputUtil.padSpaces("---------------", 18));
		System.out.print(ConsoleOutputUtil.padSpaces("--------", 15));
		System.out.println();
	}
}
