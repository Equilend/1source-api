package com.os.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.ConsoleConfig;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class SpringBootConsoleApp extends AbstractConsole implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootConsoleApp.class);

	@Autowired
	ConsoleConfig authConfig;
	
	@Autowired
	WebClient restWebClient;

	public static void main(String[] args) {
		logger.debug("STARTING THE APPLICATION");
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootConsoleApp.class, args);
		SpringApplication.exit(applicationContext);
		logger.debug("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {

		logger.debug("EXECUTING : command line runner");

		for (int i = 0; i < args.length; ++i) {
			logger.info("args[{}]: {}", i, args[i]);
		}

		Console console = System.console();
		
		if (console == null) {
			logger.warn("No console available");
			return;
		}

		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

		LoginConsole loginConsole = new LoginConsole();
		loginConsole.login(authConfig, restWebClient, consoleIn);

		if (ConsoleConfig.TOKEN == null || ConsoleConfig.ACTING_PARTY == null || ConsoleConfig.ACTING_AS == null) {
			System.exit(-2);
		}
		
		System.out.println();
		System.out.println("\"?\" or \"help\" to see menu");
		System.out.println();

		execute(consoleIn, restWebClient);
	}

	@Override
	protected void printMenu() {
		System.out.println("Main Menu");
		System.out.println("-----------------------");
		System.out.println("L - Loans");
		System.out.println("R - Returns");
		System.out.println("C - Recalls");
		System.out.println("T - Rerates");
		System.out.println("D - Delegations");
		System.out.println("E - Events");
	}

	@Override
	protected boolean prompt() {
		System.out.print(ConsoleConfig.ACTING_PARTY.getPartyId() + " > ");
		return true;
	}

	@Override
	protected void handleArgs(String[] args, BufferedReader consoleIn, WebClient webClient) {
		
		if (args[0].equalsIgnoreCase("l")) {
			LoansConsole loansConsole = new LoansConsole();
			loansConsole.execute(consoleIn, restWebClient);
		} else if (args[0].equalsIgnoreCase("r")) {
			ReturnsConsole returnsConsole = new ReturnsConsole();
			returnsConsole.execute(consoleIn, restWebClient);
		} else if (args[0].equalsIgnoreCase("c")) {
			RecallsConsole recallsConsole = new RecallsConsole();
			recallsConsole.execute(consoleIn, restWebClient);
		} else if (args[0].equalsIgnoreCase("t")) {
			ReratesConsole reratesConsole = new ReratesConsole();
			reratesConsole.execute(consoleIn, restWebClient);
		} else if (args[0].equalsIgnoreCase("d")) {
			DelegationsConsole delegationsConsole = new DelegationsConsole();
			delegationsConsole.execute(consoleIn, restWebClient);
		} else if (args[0].equalsIgnoreCase("e")) {
			EventsConsole eventsConsole = new EventsConsole();
			eventsConsole.execute(consoleIn, restWebClient);
		} else {
			System.out.println("Unknown command");
		}		
	}

}