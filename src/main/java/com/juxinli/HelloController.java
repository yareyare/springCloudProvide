package com.juxinli;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ivy on 2017/12/1.
 */
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
