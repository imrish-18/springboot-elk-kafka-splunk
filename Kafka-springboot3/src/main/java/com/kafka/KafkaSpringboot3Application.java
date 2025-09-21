package com.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class KafkaSpringboot3Application {

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication.run(KafkaSpringboot3Application.class, args);
		System.out.println(InetAddress.getByName("127.0.0.1"));
		System.out.println(InetAddress.getByName("localhost"));

	}

}
