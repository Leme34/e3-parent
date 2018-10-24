package com.Lee.e3ssoservice.conf;

import com.Lee.jedis.JedisClient;
import com.Lee.jedis.JedisClientCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.util.HashSet;
import java.util.Set;

@Configuration  //需要保证你的启动Spring Boot main入口，在这些配置类的上层包才能被扫描并注入
public class JedisBeansConf {

//===================================注入jedis单机版对象====================================
//    @Bean
//    public JedisPool jedisPool(){
//        JedisPool jedisPool = new JedisPool("192.168.11.101", 6379);
//        return jedisPool;
//    }
//
//    @Autowired
//    private JedisPool jedisPool;
//
//    @Bean
//    public JedisClient jedisClient(){
//        JedisClientPool jedisClient = new JedisClientPool();
//        jedisClient.setJedisPool(jedisPool);
//        System.out.println("jedisClient单机版已注入");
//        return jedisClient;
//    }




//======================================注入jedis集群版对象==================================

    @Bean
    public JedisCluster jedisCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.11.101",7001));
        nodes.add(new HostAndPort("192.168.11.101",7002));
        nodes.add(new HostAndPort("192.168.11.101",7003));
        nodes.add(new HostAndPort("192.168.11.101",7004));
        nodes.add(new HostAndPort("192.168.11.101",7005));
        nodes.add(new HostAndPort("192.168.11.101",7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        return jedisCluster;
    }

    @Autowired
    private JedisCluster jedisCluster;

    @Bean  //默认bean的名称就是其方法名
    public JedisClient jedisClient(){
        JedisClientCluster jedisClient = new JedisClientCluster();
        jedisClient.setJedisCluster(jedisCluster);
        System.out.println("jedisClient集群版已注入");
        return jedisClient;
    }


}
