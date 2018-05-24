package com.yoloo.server.search.entity;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "yoloo")
public class Product {

  public static final String NAME = "name";

  @Id private String id;

  @Field
  private String name;

  @Field
  private double price;

  public Product() {}

  public Product(String id, String name, double price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice() {
    return this.price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Product{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", price=" + price + '}';
  }
}
