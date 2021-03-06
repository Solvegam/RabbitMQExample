package rabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * Created by Stanislav Lentsov
 */
public class CobbReceiver {
	public static void main(String[] args) {
		connectToRabbit();
	}

	private static void connectToRabbit() {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(DataStore.HOST);
		connectionFactory.setUsername(DataStore.LOGIN);
		connectionFactory.setPassword(DataStore.PASSWORD);

		try {
			Connection connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(DataStore.EXCHANGE_NAME, DataStore.EXCHANGE_TYPE);

			channel.queueDeclare(DataStore.QUEUE_NAME_COBM, true, false, false, null);
			channel.queueDeclare(DataStore.QUEUE_NAME_CRM, true, false, false, null);
			channel.queueBind(DataStore.QUEUE_NAME_COBM, DataStore.EXCHANGE_NAME, "COBM");
			channel.queueBind(DataStore.QUEUE_NAME_CRM, DataStore.EXCHANGE_NAME, "");

			System.out.println(" [*] Waiting for messages from CRM. To exit press CTRL+C");

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
						throws IOException {
					String message = new String(body, "UTF-8");
					System.err.println(message);
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			};
			channel.basicConsume(DataStore.QUEUE_NAME_COBM, false, consumer);
			channel.basicConsume(DataStore.QUEUE_NAME_CRM, false, consumer);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}