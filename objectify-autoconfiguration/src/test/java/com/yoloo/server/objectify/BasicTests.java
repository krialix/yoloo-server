package com.yoloo.server.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.yoloo.server.objectify.appengine.TestBase;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.googlecode.objectify.ObjectifyService.factory;
import static com.googlecode.objectify.ObjectifyService.ofy;

class BasicTests extends TestBase {

  @Test
  void idIsGenerated() {
    factory().register(Demo.class);

    // Note that 5 is not the id, it's part of the payload
    final Demo demo = new Demo();
    final Key<Demo> k = ofy().save().entity(demo).now();

    assertThat(k.getKind()).isEqualTo(demo.getClass().getSimpleName());
    assertThat(k.getId()).isEqualTo(demo.getId());
  }

  @Entity
  private static class Demo {
    @Id private Long id;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }
}
