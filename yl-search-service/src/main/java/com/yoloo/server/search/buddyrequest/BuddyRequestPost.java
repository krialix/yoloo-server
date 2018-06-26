package com.yoloo.server.search.buddyrequest;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.time.LocalDate;

@SolrDocument(collection = "buddy-request")
public class BuddyRequestPost {
  @Id private String id;

  @Field private String content;

  @Field private int fromPeople;

  @Field private int toPeople;

  @Field private String locationName;

  @Field private LocalDate fromDate;

  @Field private LocalDate toDate;

  private BuddyRequestPost() {}

  private BuddyRequestPost(Builder builder) {
    id = builder.id;
    content = builder.content;
    fromPeople = builder.fromPeople;
    toPeople = builder.toPeople;
    locationName = builder.locationName;
    fromDate = builder.fromDate;
    toDate = builder.toDate;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return newBuilder()
        .id(id)
        .content(content)
        .fromPeople(fromPeople)
        .toPeople(toPeople)
        .locationName(locationName)
        .fromDate(fromDate)
        .toDate(toDate);
  }

  public String getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public int getFromPeople() {
    return fromPeople;
  }

  public int getToPeople() {
    return toPeople;
  }

  public String getLocationName() {
    return locationName;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public static final class Builder {
    private String id;
    private String content;
    private int fromPeople;
    private int toPeople;
    private String locationName;
    private LocalDate fromDate;
    private LocalDate toDate;

    private Builder() {}

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder content(String val) {
      content = val;
      return this;
    }

    public Builder fromPeople(int val) {
      fromPeople = val;
      return this;
    }

    public Builder toPeople(int val) {
      toPeople = val;
      return this;
    }

    public Builder locationName(String val) {
      locationName = val;
      return this;
    }

    public Builder fromDate(LocalDate val) {
      fromDate = val;
      return this;
    }

    public Builder toDate(LocalDate val) {
      toDate = val;
      return this;
    }

    public BuddyRequestPost build() {
      return new BuddyRequestPost(this);
    }
  }
}
