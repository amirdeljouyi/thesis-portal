package com.amideljuyi.thesisportal.repository.search;

import com.amideljuyi.thesisportal.domain.Thesis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Thesis entity.
 */
public interface ThesisSearchRepository extends ElasticsearchRepository<Thesis, Long> {
}
