package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Thesis;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Thesis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThesisRepository extends JpaRepository<Thesis,Long> {
    
}
