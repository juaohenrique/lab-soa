package br.com.jh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import br.com.jh.contracts.AggregatorServiceGrpc;

@Configuration
public class GrpcClientsConfig {

    @Bean
    AggregatorServiceGrpc.AggregatorServiceBlockingStub aggregatorServiceStub(
            GrpcChannelFactory channels
    ) {
        // "aggregatorService" é o nome do canal definido no application.yml
        return AggregatorServiceGrpc.newBlockingStub(
                channels.createChannel("aggregatorService")
        );
    }
}