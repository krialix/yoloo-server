package com.yoloo.server.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.resource.ClassPathResource;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static spark.Spark.get;

public class SearchApplication implements SparkApplication {

  private static final Logger logger = LoggerFactory.getLogger(SearchApplication.class);

  private static final ObjectMapper mapper = new ObjectMapper();

  //private final String CONFIGSET_DIR = getClass().getResource("configsets").getPath();

  private static SolrClient solrClient;

  public static void main(String[] args) {
    new SearchApplication().init();
  }

  @Override
  public void init() {
    String targetLocation =
        EmbeddedSolrServerFactory.class
            .getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getFile()
            + "/..";

    String solrHome = targetLocation + "/solr";
    String configDir = EmbeddedSolrServerFactory.class
        .getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .getFile() + "configsets";

    logger.info("PATH: {}", targetLocation);

    String configsets = null;
    try {
      configsets = Paths.get(new ClassPathResource("configsets").getURI()).toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.info("Config Sets Path: {}", configsets);

    try {
      solrClient = EmbeddedSolrServerFactory.create(solrHome, configsets, "exampleCollection");
    } catch (IOException | SolrServerException e) {
      e.printStackTrace();
    }

    // create some test documents
    SolrInputDocument doc1 = new SolrInputDocument();
    doc1.addField("id", "1");

    SolrInputDocument doc2 = new SolrInputDocument();
    doc2.addField("id", "2");

    SolrInputDocument doc3 = new SolrInputDocument();
    doc3.addField("id", "3");

    SolrInputDocument doc4 = new SolrInputDocument();
    doc4.addField("id", "4");

    SolrInputDocument doc5 = new SolrInputDocument();
    doc5.addField("id", "5");

    // add the test data to the index
    try {
      solrClient.add(Arrays.asList(doc1, doc2, doc3, doc4, doc5));
      solrClient.commit();
    } catch (SolrServerException | IOException e) {
      e.printStackTrace();
    }

    get(
        "/docs",
        (req, res) -> {
          SolrQuery solrQuery = new SolrQuery("*:*");

          return solrClient.query(solrQuery).getResults();
        });

    get("/hello", (req, res) -> "Hello");
  }

  @WebFilter(
    filterName = "SparkInitFilter",
    urlPatterns = {"/*"},
    initParams = {
      @WebInitParam(name = "applicationClass", value = "com.yoloo.server.search.SearchApplication")
    }
  )
  public static class SparkInitFilter extends SparkFilter {}
}
