package com.mq.rabbitmq.work_queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * Created by thy on 17-6-6.
 */
public class NewTask {
	
	private static final String TASK_QUEUE_NAME = "task_queue";
	public static void main(String[] argv) throws Exception {
		Scanner in = new Scanner(System.in);
		while(true){
			String input=in.next();//获取输入，以空格区分
			if("$$$".equals(input)){
				System.exit(0);
			}
			send(input.split(" "));
		}
	}
	
	private static void send(String[] argv) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		
		
		
		
//		boolean durable = true;   持久化操作  MessageProperties.PERSISTENT_TEXT_PLAIN,
//		channel.queueDeclare("task_queue", durable, false, false, null);
		
		String message = getMessage(argv);
		
		channel.basicPublish("", TASK_QUEUE_NAME,
				MessageProperties.PERSISTENT_TEXT_PLAIN,
				message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
	}
	
	private static String getMessage(String[] strings) {
		if (strings.length < 1)
			return "Hello World!";
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