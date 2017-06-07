package com.mq.rabbitmq.work_queue;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by thy on 17-6-6.
 */
public class Worker {
	
	private static final String TASK_QUEUE_NAME = "task_queue";
	
	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();
		
		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		channel.basicQos(1);// accept only one unack-ed message at a time (see below)
		
		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				
				System.out.println(" [x] Received '" + message + "'");
				try {
					doWork(message);
				} finally {
					System.out.println(" [x] Done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		
//		boolean autoAck = false;  worker下线时不丢失
//		channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
		channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
	}
	
	private static void doWork(String task) {
		for (char ch : task.toCharArray()) {
			if (ch == '.') {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException _ignored) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
//	Message acknowledgments are turned on by default. In previous examples we explicitly turned them off
// via the autoAck=true flag. It's time to set this flag to false and send a proper acknowledgment from the worker, once we're done with a task.