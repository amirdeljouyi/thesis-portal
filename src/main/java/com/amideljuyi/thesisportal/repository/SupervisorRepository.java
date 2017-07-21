package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Supervisor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Supervisor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor,Long> {
    
}
