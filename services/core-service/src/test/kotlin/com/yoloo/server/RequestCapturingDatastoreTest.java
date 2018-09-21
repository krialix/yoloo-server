package com.yoloo.server;

import com.google.cloud.datastore.FullEntity;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.yoloo.server.appengine.TestBase;
import com.yoloo.server.util.RequestCapturingAsyncDatastore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.googlecode.objectify.ObjectifyService.ofy;

class RequestCapturingDatastoreTest extends TestBase {

  @BeforeEach
  void setUp() {
    ofy().factory().register(DemoEntity.class);
  }

  @Test
  void getPuts_whenEntitySaved_willReturnPutCount1() {
    ofy().save().entity(new DemoEntity());

    List<List<FullEntity<?>>> puts = RequestCapturingAsyncDatastore.getPuts();
    assertThat(puts).hasSize(1);
  }

  @Test
  void getPuts_whenEntitySaved_willReturnPutCount2() {
    ofy().save().entity(new DemoEntity());
    ofy().save().entity(new DemoEntity());

    List<List<FullEntity<?>>> puts = RequestCapturingAsyncDatastore.getPuts();
    assertThat(puts).hasSize(2);
  }

  @Entity
  private static class DemoEntity {
    @Id private Long id;

    public DemoEntity() {}

    public DemoEntity(Long id) {
      this.id = id;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }
}
