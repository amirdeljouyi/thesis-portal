package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Professor;
import com.amideljuyi.thesisportal.domain.Referee;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Referee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefereeRepository extends JpaRepository<Referee,Long> {
    Page<Referee> findByProfessor(Professor professor, Pageable pageable);
    
}
