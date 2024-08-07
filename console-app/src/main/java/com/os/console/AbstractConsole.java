package com.os.console;

import java.io.BufferedReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.console.api.ConsoleConfig;

public abstract class AbstractConsole {

	private static final Logger logger = LoggerFactory.getLogger(AbstractConsole.class);

	protected abstract void printMenu();
	protected abstract void prompt();
	protected abstract void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient);

	public void execute(BufferedReader consoleIn, WebClient webClient) {

		String input = null;

		prompt();

		try {
			while ((input = consoleIn.readLine()) != null) {

				String[] args = parseArgs(input);
				if (args.length == 0) {
					prompt();
					continue;
				}

				if (checkSystemCommand(args[0])) {
					continue;
				} else if (goBackMenu(args[0])) {
					break;
				} else {
					handleArgs(args, consoleIn, webClient);
				}
				
				prompt();
			}
		} catch (Exception e) {
			logger.error("Exception with rerates command: " + input);
			e.printStackTrace();
		}

	}

	/**
	 * The program accepts 2 args. The second arg can be a multi-word string.
	 * @param input
	 * @return
	 */
	protected String[] parseArgs(String input) {
		ArrayList<String> argList = new ArrayList<>();
		
		if (input.trim().length() > 0) {
			String[] args = input.split(" ");
			for (int i=0; i<args.length; i++) {
				if (args[i].trim().length() == 0) {
					continue;
				} else {
					argList.add(i == 0 ? args[i].toUpperCase() : args[i]);
				}
			}
		}
		
		if (argList.size() <= 1) {
			return argList.toArray(new String[0]);
		}
		
		String[] parsedArgs = new String[2];
		parsedArgs[0] = argList.get(0);
		parsedArgs[1] = "";
		
		for (int i=1; i<argList.size(); i++) {
			parsedArgs[1] += argList.get(i);
			parsedArgs[1] += " ";
		}
		parsedArgs[1] = parsedArgs[1].trim();
		
		return parsedArgs;
	}
	
	protected boolean checkSystemCommand(String command) {

		boolean systemCommand = false;
		
		if (command.equals("?") || command.equals("HELP")) {
			System.out.println();
			printMenu();
			System.out.println();
			prompt();
			systemCommand = true;
		} else if (command.equals("QUIT") || command.equals("EXIT")
				|| command.equals("Q")) {
			System.exit(0);
		} else if (command.equals("WHOAMI")) {
			System.out.println();
			System.out.println(ConsoleConfig.ACTING_PARTY + " acting as " + ConsoleConfig.ACTING_AS);
			System.out.println();
			prompt();
			systemCommand = true;
		}

		return systemCommand;
	}

	protected boolean goBackMenu(String command) {

		boolean goBack = false;
		
		if (command.equals("X") || command.equals("..")) {
			goBack = true;
		}

		return goBack;
	}
}
