package com.yoloo.server.pubsub.emulator;

import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.autoconfigure.pubsub.GcpPubSubProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication
public class PubsubApplication {

  public static void main(String[] args) {
    SpringApplication.run(PubsubApplication.class, args);
  }

  @Bean
  public ManagedChannel managedChannel(GcpPubSubProperties gcpPubSubProperties) {
    return ManagedChannelBuilder.forTarget(gcpPubSubProperties.getEmulatorHost())
        .usePlaintext()
        .build();
  }

  @Bean
  public TransportChannelProvider transportChannelProvider(ManagedChannel channel) {
    return FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
  }

  @Bean
  public ApplicationListener<ContextClosedEvent> handleContextClosed(ManagedChannel channel) {
    return event -> channel.shutdownNow();
  }

  /*@Bean
  public RouterFunction<ServerResponse> createTopic(PubSubAdmin pubSubAdmin) {
    return route(
        POST("/topics"),
        request ->
            request
                .bodyToMono(CreateTopicRequest.class)
                .map(CreateTopicRequest::getTopicName)
                .doOnSuccess(pubSubAdmin::createTopic)
                .flatMap(
                    topicName ->
                        ServerResponse.created(URI.create("/topics/" + topicName)).build()));
  }*/
}
