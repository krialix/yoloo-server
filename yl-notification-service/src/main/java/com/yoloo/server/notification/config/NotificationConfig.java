package com.yoloo.server.notification.config;

import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.yoloo.server.notification.mapper.NotificationResponseMapper;
import com.yoloo.server.notification.usecase.ListNotificationsUseCase;
import com.yoloo.server.objectify.configuration.ObjectifyConfigurer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.gcp.autoconfigure.pubsub.GcpPubSubProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Configuration
public class NotificationConfig {

  @Bean
  @Lazy
  public NotificationResponseMapper notificationResponseMapper() {
    return new NotificationResponseMapper();
  }

  @Bean
  @Lazy
  public ListNotificationsUseCase listNotificationsUseCase(NotificationResponseMapper mapper) {
    return new ListNotificationsUseCase(mapper);
  }

  @Bean
  public ObjectifyConfigurer objectifyConfigurer() {
    return new NotificationObjectifyConfig();
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
