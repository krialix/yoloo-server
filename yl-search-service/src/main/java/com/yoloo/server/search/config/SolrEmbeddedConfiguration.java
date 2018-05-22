package com.yoloo.server.search.config;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.core.NodeConfig;
import org.apache.solr.core.SolrResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class SolrEmbeddedConfiguration {

  private static Logger logger = LoggerFactory.getLogger(SolrEmbeddedConfiguration.class);

  private final Environment env;

  public SolrEmbeddedConfiguration(Environment env) {
    this.env = env;
  }

  public String coreName() {
    return "solrCollection";
  }

  @Bean
  public SolrClient solrClient() throws IOException, SolrServerException {
    String targetLocation = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX).getPath();

    String solrHome = targetLocation;

    final File solrHomeDir = new File(solrHome);
    if (solrHomeDir.exists()) {
      FileUtils.deleteDirectory(solrHomeDir);
    }

    solrHomeDir.mkdirs();

    logger.info(String.format("Deploying SolrEmbedded to %s", solrHomeDir.getAbsolutePath()));

    final SolrResourceLoader loader = new SolrResourceLoader(solrHomeDir.toPath());
    final Path configSetPath = Paths.get(solrHome).toAbsolutePath();

    final NodeConfig config =
        new NodeConfig.NodeConfigBuilder("embeddedSolrServerNode", loader)
            .setConfigSetBaseDirectory(configSetPath.toString())
            .build();

    final EmbeddedSolrServer embeddedSolrServer = new EmbeddedSolrServer(config, coreName());

    final CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
    createRequest.setCoreName(coreName());
    createRequest.setConfigSet(coreName());
    embeddedSolrServer.request(createRequest);

    return embeddedSolrServer;
  }

  @PreDestroy()
  public void stop() {
    try {
      solrClient().close();
    } catch (Exception ignored) {
    }
  }
}
