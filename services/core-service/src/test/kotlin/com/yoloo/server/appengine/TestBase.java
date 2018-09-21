package com.yoloo.server.appengine;

import com.googlecode.objectify.Key;
import com.yoloo.server.extensions.LocalDatastoreExtension;
import com.yoloo.server.extensions.LocalMemcacheExtension;
import com.yoloo.server.extensions.ObjectifyExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * All tests should extend this class to set up the GAE environment.
 *
 * @see <a href="http://code.google.com/appengine/docs/java/howto/unittesting.html">Unit Testing in
 *     Appengine</a>
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
@ExtendWith({
  LocalDatastoreExtension.class,
  LocalMemcacheExtension.class,
  ObjectifyExtension.class,
})
public class TestBase {

  protected <E> E saveClearLoad(final E thing) {
    final Key<E> key = ofy().save().entity(thing).now();
    ofy().clear();
    return ofy().load().key(key).now();
  }
}
