package com.os.console;

import com.os.console.api.ConsoleConfig;

public abstract class AbstractConsole {

	protected abstract void printMenu();
	protected abstract void prompt();
	
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
