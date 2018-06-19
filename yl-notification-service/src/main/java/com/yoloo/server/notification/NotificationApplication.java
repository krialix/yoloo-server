package com.yoloo.server.notification;

import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.autoconfigure.pubsub.GcpPubSubProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NotificationApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationApplication.class, args);
  }

  @Profile("dev")
  @Bean
  public TransportChannelProvider transportChannelProvider(
      GcpPubSubProperties gcpPubSubProperties) {
    ManagedChannel channel =
        ManagedChannelBuilder.forTarget(gcpPubSubProperties.getEmulatorHost())
            .usePlaintext(true)
            .build();

    return FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
  }
}
