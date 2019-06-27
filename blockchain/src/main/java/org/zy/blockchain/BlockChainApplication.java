package org.zy.blockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class BlockChainApplication extends SpringBootServletInitializer{


	public static void main(String[] args) {
		SpringApplication.run(BlockChainApplication.class, args);
	}
	
}
