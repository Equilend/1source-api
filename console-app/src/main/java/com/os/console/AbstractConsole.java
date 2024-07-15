package com.os.console;

import java.util.ArrayList;

import com.os.console.api.ConsoleConfig;

public abstract class AbstractConsole {

	protected abstract void printMenu();
	protected abstract void prompt();

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
