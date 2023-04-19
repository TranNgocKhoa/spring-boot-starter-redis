package io.github.tranngockhoa.redis.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties({RedisProperties.class})
public class RedisConnectionConfiguration {
    @Bean
    public ClientOptions clientOptions() {
        return ClientOptions.builder()
                .autoReconnect(true)
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .build();
    }

    @Bean(destroyMethod = "shutdown")
    public ClientResources clientResources() {
        return DefaultClientResources.builder()
                .ioThreadPoolSize(4)
                .computationThreadPoolSize(4)
                .build();
    }

    @Bean
    public RedisSentinelConfiguration sentinelConfiguration(RedisProperties redisProperties) {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();

        sentinelConfig.setDatabase(redisProperties.getDatabase());
        sentinelConfig.setPassword(redisProperties.getSentinel().getPassword());
        sentinelConfig.setMaster(redisProperties.getSentinel().getMaster());

        List<RedisNode> redisNodeList = redisProperties.getSentinel()
                .getNodes()
                .stream()
                .map(node -> node.split(":"))
                .map(nodeArr -> new RedisNode(nodeArr[0], Integer.parseInt(nodeArr[1])))
                .collect(Collectors.toList());

        sentinelConfig.setSentinels(redisNodeList);

        return sentinelConfig;
    }

    @Bean
    public LettucePoolingClientConfiguration lettucePoolConfig(RedisProperties redisProperties, ClientOptions clientOptions, ClientResources clientResources) {
        LettucePoolingClientConfigurationBuilder clientConfigBuilder = LettucePoolingClientConfiguration.builder();
        if (redisProperties.isSsl()) {
            clientConfigBuilder.useSsl();
        }

        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        RedisProperties.Pool poolProperties = redisProperties.getLettuce().getPool();

        poolConfig.setMinIdle(poolProperties.getMinIdle());
        poolConfig.setMaxIdle(poolProperties.getMaxIdle());
        poolConfig.setMaxTotal(poolProperties.getMaxActive());
        poolConfig.setMaxWait(Duration.ofMillis(poolProperties.getMaxWait().toMillis()));

        return clientConfigBuilder.poolConfig(poolConfig)
                .clientOptions(clientOptions)
                .clientResources(clientResources)
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisSentinelConfiguration sentinelConfiguration, LettucePoolingClientConfiguration lettucePoolConfig) {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(sentinelConfiguration, lettucePoolConfig);

        lettuceConnectionFactory.setShareNativeConnection(false);

        return lettuceConnectionFactory;
    }
}
