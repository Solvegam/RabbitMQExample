package com.rabbit;

import java.util.Arrays;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class CombSender {

	private final static String QUEUE_NAME = "durability test";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(DataStore.HOST);
		factory.setUsername(DataStore.LOGIN);
		factory.setPassword(DataStore.PASSWORD);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel(DataStore.CHANEL_INDEX);

		channel.exchangeDeclare(DataStore.EXCHANGE_NAME, DataStore.EXCHANGE_TYPE);

		String message = getMessage(argv);
		channel.basicPublish(DataStore.EXCHANGE_NAME, "COBM", null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}

	private static String getMessage(String[] args) {
		if (args.length < 1) {
			return "Default message from COMB";
		}
		return "COMB           " + Arrays.stream(args).reduce((firstPart, secondPart) -> firstPart + " " + secondPart).get();
	}
}
