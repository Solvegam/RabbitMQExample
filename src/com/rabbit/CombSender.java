package com.rabbit;

import java.util.Arrays;

public class CombSender {

	private final static String QUEUE_NAME = "durability test";

	public static void main(String[] argv) throws Exception {
		RabbitConnector rabbitConnector = new RabbitConnector();
		String message = getMessage(argv);
		rabbitConnector.sendMessage(message, "COBM", null);
		System.out.println(" [x] Sent '" + message + "'");
	}

	private static String getMessage(String[] args) {
		if (args.length < 1) {
			return "Default message from COMB";
		}
		return "COMB           " + Arrays.stream(args).reduce((firstPart, secondPart) -> firstPart + " " + secondPart).get();
	}
}
