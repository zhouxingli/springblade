package org.springblade.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springblade.core.launch.constant.CommonConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SystemConfig {
	@Bean
	public Queue tableQueue(){
		Queue queue = new Queue(CommonConstant.API_RESOURCE);
		return queue;
	}
}
