package com.os.console;

import java.io.BufferedReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Party;
import com.os.client.model.PartyRole;
import com.os.console.api.ConsoleConfig;
import com.os.console.api.tasks.AuthTask;
import com.os.console.api.tasks.SearchPartyTask;

public class LoginConsole {

	private static final Logger logger = LoggerFactory.getLogger(LoginConsole.class);

	public void login(ConsoleConfig authConfig, WebClient webClient, BufferedReader consoleIn) {

		try {

			if (authConfig.getAuth_username() == null) {
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

				System.out.print("Party: ");
				String party = consoleIn.readLine();
				if (party == null || party.length() == 0) {
					System.exit(-1);
				}

				authConfig.setAuth_username(username);
				authConfig.setAuth_password(password);
				authConfig.setAuth_party(party);
			} else {
				System.out.println("Using properties for authentication");
			}

			System.out.print("Authenticating..." + authConfig.getAuth_uri());

			AuthTask authTask = new AuthTask(authConfig);
			Thread taskT = new Thread(authTask);
			taskT.run();
			try {
				taskT.join();
				if (ConsoleConfig.TOKEN != null) {
					System.out.println("success");
				} else {
					System.out.println("invalid username and/or password");
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Connecting..." + authConfig.getApi_uri());

			System.out.print("Verify acting as...");

			PartyRole actingAs = PartyRole.fromValue(authConfig.getAuth_actAs());
			if (actingAs != null) {
				ConsoleConfig.ACTING_AS = actingAs;
				System.out.println(ConsoleConfig.ACTING_AS);
			} else {
				System.out.println("act as not set properly");
				return;
			}

			if (PartyRole.VENUE.equals(ConsoleConfig.ACTING_AS)) {
				Party venueParty = new Party();
				venueParty.setPartyId(authConfig.getAuth_party());
				venueParty.setPartyName("Venue " + authConfig.getAuth_party());
				venueParty.setGleifLei("213800BN4DRR1ADYGP92");
				ConsoleConfig.ACTING_PARTY = venueParty;
				System.out.println("TODO - Authorize venue party");
			} else {
				System.out.print("Authorizing " + authConfig.getAuth_party() + "...");

				SearchPartyTask searchPartyTask = new SearchPartyTask(webClient, authConfig.getAuth_party());
				taskT = new Thread(searchPartyTask);
				taskT.run();
				try {
					taskT.join();
					if (searchPartyTask.getParty() != null) {
						ConsoleConfig.ACTING_PARTY = searchPartyTask.getParty();
					} else {
						System.out.println("acting party not recognized");
						return;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("Error during login");
			logger.error("Exception during login: " + e.getMessage());
		} finally {
		}

	}

}
