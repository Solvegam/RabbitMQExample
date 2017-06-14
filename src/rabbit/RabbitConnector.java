package rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by Stanislav Lentsov
 */
public final class RabbitConnector {

	private Channel channel;
	private Connection connection;

	public RabbitConnector() throws Exception  {
		connect();
	}

	public void sendMessage(String message, String routingKey, AMQP.BasicProperties properties) throws Exception {
		channel.basicPublish(DataStore.EXCHANGE_NAME, routingKey, properties, message.getBytes("UTF-8"));
		disconnect();
	}

	private void connect() throws Exception {
		if (channel == null) {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(DataStore.HOST);
			factory.setUsername(DataStore.LOGIN);
			factory.setPassword(DataStore.PASSWORD);
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(DataStore.EXCHANGE_NAME, DataStore.EXCHANGE_TYPE);
		}
	}

	private void disconnect() throws Exception {
		if (channel != null) {
			channel.close();
			connection.close();
			channel = null;
			connection = null;
		}
	}

}
