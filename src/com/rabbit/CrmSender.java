package com.rabbit;

import java.util.Arrays;

import com.rabbitmq.client.MessageProperties;

public class CrmSender {

	public static void main(String[] argv) throws Exception {
		RabbitConnector rabbitConnector = new RabbitConnector();
		String message = getMessage(argv);
		rabbitConnector.sendMessage(message, "", MessageProperties.PERSISTENT_TEXT_PLAIN);
		System.out.println(" [x] Sent '" + message + "'");
	}

	private static String getMessage(String[] args) {
		if (args.length < 1) {
			return "Default message from CRM";
		}
		return "CRM           " + Arrays.stream(args).reduce((firstPart, sesondPart) -> firstPart + " " + sesondPart).get();
	}
}
