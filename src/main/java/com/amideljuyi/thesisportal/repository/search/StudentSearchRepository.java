package com.amideljuyi.thesisportal.repository.search;

import com.amideljuyi.thesisportal.domain.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Student entity.
 */
public interface StudentSearchRepository extends ElasticsearchRepository<Student, Long> {
}
