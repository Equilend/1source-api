package com.os.console;

import java.io.BufferedReader;
import java.util.UUID;

import org.springframework.web.reactive.function.client.WebClient;

import com.os.client.model.Rerate;
import com.os.console.api.tasks.ApproveRerateTask;
import com.os.console.api.tasks.CancelRerateTask;
import com.os.console.api.tasks.DeclineRerateTask;
import com.os.console.api.tasks.SearchRerateTask;
import com.os.console.api.tasks.SearchReratesTask;

public class ReratesConsole extends AbstractConsole {

	protected boolean prompt() {
		System.out.print("/rerates > ");
		return true;
	}

	public void handleArgs(String args[], BufferedReader consoleIn, WebClient webClient) {

		if (args[0].equals("I")) {
			System.out.print("Listing all rerates...");
			SearchReratesTask searchReratesTask = new SearchReratesTask(webClient);
			Thread taskT = new Thread(searchReratesTask);
			taskT.run();
			try {
				taskT.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("S")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Retrieving rerate " + rerateId + "...");
						SearchRerateTask searchRerateTask = new SearchRerateTask(webClient, rerateId);
						Thread taskT = new Thread(searchRerateTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchRerateTask.getRerate() != null) {
							RerateConsole rerateConsole = new RerateConsole(searchRerateTask.getRerate());
							rerateConsole.execute(consoleIn, webClient);
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("A")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Retrieving rerate " + rerateId + "...");
						SearchRerateTask searchRerateTask = new SearchRerateTask(webClient, rerateId);
						Thread taskT = new Thread(searchRerateTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchRerateTask.getRerate() != null) {
							Rerate rerate = searchRerateTask.getRerate();
							System.out.print("Approving rerate...");
							ApproveRerateTask approveRerateTask = new ApproveRerateTask(webClient, rerate.getLoanId(), rerate.getRerateId());
							Thread taskS = new Thread(approveRerateTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("C")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Retrieving rerate " + rerateId + "...");
						SearchRerateTask searchRerateTask = new SearchRerateTask(webClient, rerateId);
						Thread taskT = new Thread(searchRerateTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchRerateTask.getRerate() != null) {
							Rerate rerate = searchRerateTask.getRerate();
							System.out.print("Canceling rerate...");
							CancelRerateTask cancelRerateTask = new CancelRerateTask(webClient, rerate.getLoanId(), rerate.getRerateId());
							Thread taskS = new Thread(cancelRerateTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else if (args[0].equals("D")) {
			if (args.length != 2 || args[1].length() != 36) {
				System.out.println("Invalid UUID");
			} else {
				String rerateId = args[1];
				try {
					if (UUID.fromString(rerateId).toString().equals(rerateId)) {
						System.out.print("Retrieving rerate " + rerateId + "...");
						SearchRerateTask searchRerateTask = new SearchRerateTask(webClient, rerateId);
						Thread taskT = new Thread(searchRerateTask);
						taskT.run();
						try {
							taskT.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (searchRerateTask.getRerate() != null) {
							Rerate rerate = searchRerateTask.getRerate();
							System.out.print("Declining rerate...");
							DeclineRerateTask declineRerateTask = new DeclineRerateTask(webClient, rerate.getLoanId(), rerate.getRerateId());
							Thread taskS = new Thread(declineRerateTask);
							taskS.run();
							try {
								taskS.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Invalid UUID");
					}
				} catch (Exception u) {
					System.out.println("Invalid UUID");
				}
			}
		} else {
			System.out.println("Unknown command");
		}
	}

	protected void printMenu() {
		System.out.println("Rerates Menu");
		System.out.println("-----------------------");
		System.out.println("I             - List all rerates");
		System.out.println("S <Rerate Id> - Load a rerate by Id");
		System.out.println();
		System.out.println("A <Rerate Id> - Approve a rerate by Id");
		System.out.println("C <Rerate Id> - Cancel a rerate by Id");
		System.out.println("D <Rerate Id> - Decline a rerate by Id");
		System.out.println();
		System.out.println("X             - Go back");
	}

}
