package com.yoloo.server.search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;

/** @author bbende */
public class EmbeddedSolrServerFactoryOld {

  /**
   * Cleans the given solrHome directory and creates a new EmbeddedSolrServer.
   *
   * @param solrHome the Solr home directory to use
   * @param configSetHome the directory containing config sets
   * @param coreName the name of the core, must have a matching directory in configHome
   * @return an EmbeddedSolrServer with a core created for the given coreName
   */
  public static SolrClient create(String solrHome, String coreName) {
    CoreContainer coreContainer = new CoreContainer(solrHome);
    coreContainer.load();

    return new EmbeddedSolrServer(coreContainer, coreName);
  }
}
