package com.yoloo.server.search.buddyrequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BuddyRequestRepository extends SolrCrudRepository<BuddyRequestPost, String> {

  @Query(
      "fromPeople:[?0 TO *] OR toPeople:[* TO ?1] OR locationName:?2 OR fromDate:[?3 TO *] OR toDate[* TO ?4]")
  Page<BuddyRequestPost> searchBuddies(
      Integer fromPeople,
      Integer toPeople,
      String locationName,
      LocalDate fromDate,
      LocalDate toDate,
      Pageable pageable);
}
