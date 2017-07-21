package com.amideljuyi.thesisportal.repository.search;

import com.amideljuyi.thesisportal.domain.Level;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Level entity.
 */
public interface LevelSearchRepository extends ElasticsearchRepository<Level, Long> {
}
