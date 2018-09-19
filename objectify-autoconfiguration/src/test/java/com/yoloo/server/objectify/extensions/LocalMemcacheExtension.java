package com.yoloo.server.objectify.extensions;

import net.spy.memcached.MemcachedClient;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/** Resets a local memcached on every test run */
public class LocalMemcacheExtension implements BeforeAllCallback, BeforeEachCallback {

  private static final Logger log = LoggerFactory.getLogger(LocalMemcacheExtension.class);

  public LocalMemcacheExtension() {}

  /** Get the helper created in beforeAll; it should be global so there will one per test run */
  public static MemcachedClient getClient(final ExtensionContext context) {
    return context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .get(MemcachedClient.class, MemcachedClient.class);
  }

  @Override
  public void beforeAll(final ExtensionContext context) throws Exception {
    if (getClient(context) == null) {
      log.info("Creating new MemcachedClient");

      final MemcachedClient client = new MemcachedClient(new InetSocketAddress("localhost", 11211));
      context
          .getRoot()
          .getStore(ExtensionContext.Namespace.GLOBAL)
          .put(MemcachedClient.class, client);
    }
  }

  @Override
  public void beforeEach(final ExtensionContext context) {
    final MemcachedClient client = getClient(context);
    client.flush();
  }
}
