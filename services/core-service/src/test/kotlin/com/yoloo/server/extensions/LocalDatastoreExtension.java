package com.yoloo.server.extensions;

import com.google.cloud.datastore.testing.LocalDatastoreHelper;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Sets up and tears down the Local Datastore emulator, defaults to strong consistency */
public class LocalDatastoreExtension implements BeforeAllCallback, BeforeEachCallback {

  private static final Logger log = LoggerFactory.getLogger(LocalDatastoreExtension.class);

  private final double consistency;

  public LocalDatastoreExtension() {
    this(1.0);
  }

  private LocalDatastoreExtension(double consistency) {
    this.consistency = consistency;
  }

  /** Get the helper created in beforeAll; it should be global so there will one per test run */
  static LocalDatastoreHelper getHelper(final ExtensionContext context) {
    return context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .get(LocalDatastoreHelper.class, LocalDatastoreHelper.class);
  }

  @Override
  public void beforeAll(final ExtensionContext context) throws Exception {
    if (getHelper(context) == null) {
      log.info("Creating new LocalDatastoreHelper");

      final LocalDatastoreHelper helper = LocalDatastoreHelper.create(consistency);
      context
          .getRoot()
          .getStore(ExtensionContext.Namespace.GLOBAL)
          .put(LocalDatastoreHelper.class, helper);
      helper.start();
    }
  }

  @Override
  public void beforeEach(final ExtensionContext context) throws Exception {
    final LocalDatastoreHelper helper = getHelper(context);
    helper.reset();
  }
}
