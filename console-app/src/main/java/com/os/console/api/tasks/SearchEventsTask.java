package com.os.console.api.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Event;
import com.os.client.model.Events;
import com.os.console.util.ConsoleOutputUtil;
import com.os.console.util.RESTUtil;

public class SearchEventsTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SearchEventsTask.class);

	private WebClient webClient;

	public SearchEventsTask(WebClient webClient) {
		this.webClient = webClient;
	}

	@Override
	public void run() {

		Events events = (Events) RESTUtil.getRequest(webClient, "/events", Events.class);

		if (events == null || events.size() == 0) {
			logger.warn("Invalid events object or no events");
			System.out.println("no events found");
			printHeader();
		} else {
			System.out.println("complete");
			printHeader();
			int rows = 1;
			for (Event event : events) {
				if (rows % 15 == 0) {
					printHeader();
				}

				System.out.print(ConsoleOutputUtil.padSpaces(event.getEventId(), 25));
				System.out.print(ConsoleOutputUtil.padSpaces(event.getEventDateTime(), 30));
				System.out.print(ConsoleOutputUtil.padSpaces(event.getEventType().toString(), 35));

				System.out.println();

				rows++;
			}
		}
		System.out.println();
	}

	public void printHeader() {
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("Event Id", 25));
		System.out.print(ConsoleOutputUtil.padSpaces("Date Time", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("Type", 35));
		System.out.println();
		System.out.print(ConsoleOutputUtil.padSpaces("-----------", 25));
		System.out.print(ConsoleOutputUtil.padSpaces("--------", 30));
		System.out.print(ConsoleOutputUtil.padSpaces("------", 35));
		System.out.println();
	}
}
