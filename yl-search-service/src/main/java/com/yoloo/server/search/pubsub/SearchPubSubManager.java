package com.yoloo.server.search.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cinnom.nanocuckoo.NanoCuckooFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;

//@Component
public class SearchPubSubManager {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final long DUPLICATE_FILTER_CAPACITY = 100_000L;

  private final PubSubTemplate pubSubTemplate;
  private final ObjectMapper mapper;
  private final NanoCuckooFilter duplicateFilter;

  //@Autowired
  public SearchPubSubManager(PubSubTemplate pubSubTemplate, ObjectMapper mapper) {
    this.pubSubTemplate = pubSubTemplate;
    this.mapper = mapper;
    this.duplicateFilter = buildDuplicateFilter();
    listenPubSubEvents();
  }

  private static NanoCuckooFilter buildDuplicateFilter() {
    return new NanoCuckooFilter.Builder(DUPLICATE_FILTER_CAPACITY).build();
  }

  private void listenPubSubEvents() {
    pubSubTemplate
        .subscribe("search:post", (message, consumer) -> {
          if (isDuplicate(message.getMessageId())) {
            LOGGER.info("Duplicate message found: {}", message.getMessageId());
            return;
          }

          //mapper.readValue(message.getData().toByteArray(), PostEvent.class);
        });
  }

  private boolean isDuplicate(String messageId) {
    return duplicateFilter.contains(messageId);
  }
}
