package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Rerate;
import com.os.client.model.Rerates;
import com.os.console.api.AuthConfig;
import com.os.console.util.ConsoleOutputUtil;

import reactor.core.publisher.Mono;

public class SearchReratesTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchReratesTask.class);

	private WebClient webClient;
	
	public SearchReratesTask(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public void run() {
		
		Rerates rerates = webClient.get().uri("/rerates")
				.headers(h -> h.setBearerAuth(AuthConfig.TOKEN.getAccess_token())).retrieve()
				.onStatus(HttpStatusCode.valueOf(404)::equals, response -> {
					logger.error(HttpStatus.NOT_FOUND.toString());
					return Mono.empty();
				}).bodyToMono(Rerates.class).block();

		if (rerates == null || rerates.size() == 0) {
			logger.warn("Invalid rerates object or no rerates");			
			System.out.println("no rerates found");
			printHeader();
		} else {
			System.out.println("complete");
			printHeader();
			int rows = 1;
			for (Rerate rerate : rerates) {
				if (rows % 15 == 0) {
					printHeader();
				}
				System.out.print(ConsoleOutputUtil.padSpaces(rerate.getRerateId(), 40));
				System.out.print(ConsoleOutputUtil.padSpaces(rerate.getContractId(), 40));
				System.out.print(ConsoleOutputUtil.padSpaces(rerate.getStatus().toString(), 12));
				System.out.print(ConsoleOutputUtil.padSpaces(rerate.getLastUpdateDatetime(), 30));
				System.out.print(ConsoleOutputUtil.padSpaces(rerate.getDateProposed(), 15));
				System.out.println();
				
				rows++;
			}
		}
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Rerate Id", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("Contract Id", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("Status", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("Last Update DateTime", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Date Proposed", 15));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("---------", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("-----------", 40));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 12));
		System.out.print(ConsoleOutputUtil.padSpaces("--------------------", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("-------------", 15));
		System.out.println();		
	}
}
