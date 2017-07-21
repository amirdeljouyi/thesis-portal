package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Professor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Professor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Long> {
    
}
