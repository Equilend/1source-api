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
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.AuthConfig;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringBootConsoleApp implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootConsoleApp.class);

	@Autowired
	AuthConfig authConfig;
	
	@Autowired
	WebClient restWebClient;

	public static void main(String[] args) {
		logger.info("STARTING THE APPLICATION");
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootConsoleApp.class, args);
		SpringApplication.exit(applicationContext);
		logger.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) {

		logger.info("EXECUTING : command line runner");

		for (int i = 0; i < args.length; ++i) {
			logger.info("args[{}]: {}", i, args[i]);
		}

//		Console console = System.console();
//		
//		if (console == null) {
//			logger.warn("No console available");
//			return;
//		}

		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

		LoginConsole loginConsole = new LoginConsole();
		loginConsole.login(authConfig, consoleIn);

		String command = null;
		System.out.print("> ");

		try {
			while ((command = consoleIn.readLine()) != null) {
				command = command.trim();
				if (command.equals("?") || command.equalsIgnoreCase("help")) {
					printMainHelp();
				} else if (command.equalsIgnoreCase("c")) {
					ContractsConsole contractsConsole = new ContractsConsole();
					contractsConsole.execute(consoleIn, restWebClient);
				} else {
					System.out.println("Unknown command");
				}

				System.out.print("> ");
			}
		} catch (Exception e) {
			logger.error("Exception with command: " + command);
			e.printStackTrace();
		}
	}

	private void printMainHelp() {
		System.out.println();
		System.out.println("Main Menu");
		System.out.println("-----------------------");
		System.out.println("C - Contracts");
		System.out.println();
	}

}