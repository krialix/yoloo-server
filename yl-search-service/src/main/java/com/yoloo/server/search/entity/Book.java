package com.yoloo.server.search.entity;

import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Indexed
public class Book {

  @Id @GeneratedValue private Integer id;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String title;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
  private String subtitle;

  @IndexedEmbedded @ManyToMany private Set<Author> authors = new HashSet<>();

  @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
  @DateBridge(resolution = Resolution.DAY)
  private Date publicationDate;

  public Book() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public Set<Author> getAuthors() {
    return authors;
  }

  public void setAuthors(Set<Author> authors) {
    this.authors = authors;
  }

  public Date getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(Date publicationDate) {
    this.publicationDate = publicationDate;
  }
}
