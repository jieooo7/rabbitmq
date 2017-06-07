package com.mq.rabbitmq.subscribe;

import com.rabbitmq.client.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by thy on 17-6-7.
 */
public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "logs";
	private static final transient org.slf4j.Logger log = LoggerFactory.getLogger(ReceiveLogs.class);
	
	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		
		
		//		when we supply no parameters to queueDeclare() we create a non-durable, exclusive, autodelete queue with a generated name:
//		String queueName = channel.queueDeclare().getQueue(); 获取queue名字

//		We've already created a fanout exchange and a queue. Now we need to tell the exchange to send messages to our queue.
//      That relationship between exchange and a queue is called a binding.
//		绑定队列名和exchange
//
//		channel.queueBind(queueName, "logs", "");
		
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");
		
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		log.info("队列名称:"+queueName);
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
			                           AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}