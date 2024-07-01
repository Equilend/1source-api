package com.os.console;

import java.io.BufferedReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.os.console.api.AuthConfig;
import com.os.console.api.tasks.AuthTask;

public class LoginConsole {

	private static final Logger logger = LoggerFactory.getLogger(LoginConsole.class);

	public void login(AuthConfig authConfig, BufferedReader consoleIn) {

		try {

			System.out.print("Username: ");
			String username;

			int retries = 0;
			while ((username = consoleIn.readLine()) != null) {
				username = username.trim();
				if (username.length() == 0) {
					if (retries == 3) {
						System.exit(-1);
						break;
					}
					System.out.print("Username: ");
					retries++;
					continue;
				}
				break;
			}

			System.out.print("Password: ");
			String password = consoleIn.readLine();
			if (password == null || password.length() == 0) {
				System.exit(-1);
			}

			authConfig.setAuth_username(username);
			authConfig.setAuth_password(password);
			
			System.out.print("Authenticating...");

			AuthTask authTask = new AuthTask(authConfig);
			Thread taskT = new Thread(authTask);
			taskT.run();
			try {
				taskT.join();
				System.out.println(AuthConfig.TOKEN == null ? "Invalid username and/or password." : "Success.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			logger.error("Exception during authentication" + e.getMessage());
		} finally {
		}

	}

}
