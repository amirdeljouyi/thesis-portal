package com.amideljuyi.thesisportal.repository.search;

import com.amideljuyi.thesisportal.domain.Referee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Referee entity.
 */
public interface RefereeSearchRepository extends ElasticsearchRepository<Referee, Long> {
}
