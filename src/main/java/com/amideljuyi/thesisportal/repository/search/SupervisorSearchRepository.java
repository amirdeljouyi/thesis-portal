package com.amideljuyi.thesisportal.repository.search;

import com.amideljuyi.thesisportal.domain.Supervisor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Supervisor entity.
 */
public interface SupervisorSearchRepository extends ElasticsearchRepository<Supervisor, Long> {
}
