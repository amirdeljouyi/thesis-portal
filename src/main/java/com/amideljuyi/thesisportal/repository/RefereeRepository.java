package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Referee;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Referee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefereeRepository extends JpaRepository<Referee,Long> {
    
}
