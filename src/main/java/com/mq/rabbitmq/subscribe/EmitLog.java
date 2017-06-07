package com.mq.rabbitmq.subscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.LoggerFactory;

/**
 * Created by thy on 17-6-7.
 */
public class EmitLog {
	
	private static final String EXCHANGE_NAME = "logs";
	private static final transient org.slf4j.Logger log = LoggerFactory.getLogger(EmitLog.class);
	
	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		

		
		String message = getMessage(argv);
		
		channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));//队列参数为空，使用系统默认的随机名
		System.out.println(" [x] Sent '" + message + "'");
		
		channel.close();
		connection.close();
	}
	
	private static String getMessage(String[] strings){
		if (strings.length < 1)
			return "info: Hello World!";
		return joinStrings(strings, " ");
	}
	
	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0) return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}
}
