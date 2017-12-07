package com.juxinli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient //激活Eureka中的DiscoveryClient实现（自动化配置，创建DiscoveryClient接口针对Eureka客户端的EurekaDiscoveryClient实例），才能实现Controller中对服务信息的输出
@SpringBootApplication
public class SpringCloudProvideApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudProvideApplication.class, args);
	}
}


//启动后日志中显示服务注册的信息，说明注册成功
//com.netflix.discovery.DiscoveryClient    : DiscoveryClient_HELLO-SERVICE/192.168.100.173:hello-service:8100: registering service...
//com.netflix.discovery.DiscoveryClient    : DiscoveryClient_HELLO-SERVICE/192.168.100.173:hello-service:8100 - registration status: 204

//注册中心的日志
//c.n.e.registry.AbstractInstanceRegistry  : Registered instance HELLO-SERVICE/192.168.100.173:hello-service:8100 with status UP (replication=false)