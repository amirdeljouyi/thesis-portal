package com.amideljuyi.thesisportal.repository.search;

import com.amideljuyi.thesisportal.domain.Adviser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Adviser entity.
 */
public interface AdviserSearchRepository extends ElasticsearchRepository<Adviser, Long> {
}
