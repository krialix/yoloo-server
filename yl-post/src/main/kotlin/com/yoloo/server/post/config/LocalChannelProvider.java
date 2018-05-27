package com.yoloo.server.post.config;

import com.google.api.core.BetaApi;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * InstantiatingGrpcChannelProvider is a TransportChannelProvider which constructs a gRPC
 * ManagedChannel with a number of configured inputs every time getChannel(...) is called. These
 * inputs include a port, a service address, and credentials.
 *
 * <p>The credentials can either be supplied directly (by providing a FixedCredentialsProvider to
 * Builder.setCredentialsProvider()) or acquired implicitly from Application Default Credentials (by
 * providing a GoogleCredentialsProvider to Builder.setCredentialsProvider()).
 *
 * <p>The client lib header and generator header values are used to form a value that goes into the
 * http header of requests to the service.
 */
public final class LocalChannelProvider implements TransportChannelProvider {

  @Override
  public boolean needsExecutor() {
    return false;
  }

  @Override
  public TransportChannelProvider withExecutor(ScheduledExecutorService executor) {
    return null;
  }

  @Override
  @BetaApi("The surface for customizing headers is not stable yet and may change in the future.")
  public boolean needsHeaders() {
    return false;
  }

  @Override
  @BetaApi("The surface for customizing headers is not stable yet and may change in the future.")
  public TransportChannelProvider withHeaders(Map<String, String> headers) {
    return null;
  }

  @Override
  public String getTransportName() {
    return GrpcTransportChannel.getGrpcTransportName();
  }

  @Override
  public boolean needsEndpoint() {
    return false;
  }

  @Override
  public TransportChannelProvider withEndpoint(String endpoint) {
    return null;
  }

  @Override
  @BetaApi("The surface for customizing pool size is not stable yet and may change in the future.")
  public boolean acceptsPoolSize() {
    return false;
  }

  @Override
  @BetaApi("The surface for customizing pool size is not stable yet and may change in the future.")
  public TransportChannelProvider withPoolSize(int size) {
    return null;
  }

  @Override
  public TransportChannel getTransportChannel() {
    if (needsExecutor()) {
      throw new IllegalStateException("getTransportChannel() called when needsExecutor() is true");
    } else if (needsHeaders()) {
      throw new IllegalStateException("getTransportChannel() called when needsHeaders() is true");
    } else if (needsEndpoint()) {
      throw new IllegalStateException("getTransportChannel() called when needsEndpoint() is true");
    } else {
      return createChannel();
    }
  }

  private TransportChannel createChannel() {
    ManagedChannel outerChannel = createSingleChannel();
    return GrpcTransportChannel.create(outerChannel);
  }

  private ManagedChannel createSingleChannel() {
    return ManagedChannelBuilder.forAddress("localhost", 8085).usePlaintext(true).build();
  }

  @Override
  public boolean shouldAutoClose() {
    return true;
  }
}
