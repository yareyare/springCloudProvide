基于spring boot创建 服务提供者注册到eureka的注册中心

1.引入依赖包
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>1.5.2.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka-server</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>


2.使用DiscoveryClient对象，在日志中打印服务的相关内容
@RestController
public class HelloController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @Deprecated
    public String indexBak(){
        ServiceInstance instance=client.getLocalServiceInstance();
        logger.info("/hello,host:"+instance.getHost()+",port:"+instance.getPort()+",service_id:"+instance.getServiceId());
        return "hello "+instance.getHost()+","+instance.getPort()+","+instance.getServiceId();
    }

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String index(){
        client.getServices().forEach(id -> {
            client.getInstances(id).forEach(instance -> {
                logger.info("/hello,host:"+instance.getHost()+",port:"+instance.getPort()+",service_id:"+instance.getServiceId());
            });
        });
        return "hello world";
    }
}
// 备注 client.getLocalServiceInstance();已经被标记为不推介使用，所以用client.getServices()。

3.@EnableDiscoveryClient
激活Eureka中的DiscoveryClient实现（自动化配置，创建DiscoveryClient接口针对Eureka客户端的EurekaDiscoveryClient实例），才能实现Controller中对服务信息的输出

3.配置说明
    server.port=8100
    spring.application.name=hello-service
    eureka.client.service-url.defaultZone=http://localhost:8000/eureka

    #spring.application.name 属性为服务命名
    #eureka.client.service-url.defaultZone 指定服务注册中心的地址

4.启动服务
    先启动注册中心，然后启动本服务提供者。

    启动后日志中显示服务注册的信息，说明注册成功
    com.netflix.discovery.DiscoveryClient    : DiscoveryClient_HELLO-SERVICE/192.168.100.173:hello-service:8100: registering service...
    com.netflix.discovery.DiscoveryClient    : DiscoveryClient_HELLO-SERVICE/192.168.100.173:hello-service:8100 - registration status: 204

    注册中心的日志
    c.n.e.registry.AbstractInstanceRegistry  : Registered instance HELLO-SERVICE/192.168.100.173:hello-service:8100

    访问 http://localhost:8000/ 显示spring eureka 注册中心。可以看到hello-service已经注册成功了

    访问 http://localhost:8100/hello 可以访问到接口，服务端会打印信息
     com.juxinli.HelloController              : /hello,host:192.168.100.173,port:8100,service_id:HELLO-SERVICE
     就是我们在HelloController中注入的DiscoveryClient接口对象，从服务注册中心获取的服务相关信息

     启动多台服务
     java -jar springCloudProvide-0.0.1-SNAPSHOT.jar --server.port=8101
     java -jar springCloudProvide-0.0.1-SNAPSHOT.jar --server.port=8102