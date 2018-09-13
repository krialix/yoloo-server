package com.yoloo.server.notification.config;

import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.gcp.autoconfigure.pubsub.GcpPubSubProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class NotificationConfig {

  @Profile("dev")
  @Bean
  public TransportChannelProvider transportChannelProvider(
      GcpPubSubProperties gcpPubSubProperties) {
    ManagedChannel channel =
        ManagedChannelBuilder.forTarget(gcpPubSubProperties.getEmulatorHost())
            .usePlaintext()
            .build();

    return FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
  }
}
