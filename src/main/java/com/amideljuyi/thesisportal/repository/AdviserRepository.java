package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Adviser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Adviser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdviserRepository extends JpaRepository<Adviser,Long> {
    
}
