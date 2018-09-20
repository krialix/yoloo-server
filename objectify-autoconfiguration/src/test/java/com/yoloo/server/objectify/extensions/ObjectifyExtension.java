package com.yoloo.server.objectify.extensions;

import com.google.cloud.datastore.Datastore;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.AsyncDatastore;
import com.googlecode.objectify.util.Closeable;
import com.yoloo.server.objectify.util.RequestCapturingAsyncDatastore;
import net.spy.memcached.MemcachedClient;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/** Sets up and tears down the GAE local unit test harness environment */
public class ObjectifyExtension implements BeforeEachCallback, AfterEachCallback {

  private static final ExtensionContext.Namespace NAMESPACE =
      ExtensionContext.Namespace.create(ObjectifyExtension.class);

  @Override
  public void beforeEach(final ExtensionContext context) {
    final Datastore datastore =
        LocalDatastoreExtension.getHelper(context).getOptions().getService();
    final MemcachedClient memcachedClient = LocalMemcacheExtension.getClient(context);

    ObjectifyService.init(
        new ObjectifyFactory(datastore, memcachedClient) {
          @Override
          public AsyncDatastore asyncDatastore() {
            return new RequestCapturingAsyncDatastore(super.asyncDatastore());
          }
        });

    final Closeable rootService = ObjectifyService.begin();

    context.getStore(NAMESPACE).put(Closeable.class, rootService);
  }

  @Override
  public void afterEach(final ExtensionContext context) {
    final Closeable rootService = context.getStore(NAMESPACE).get(Closeable.class, Closeable.class);
    rootService.close();
  }
}
