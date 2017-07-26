package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Professor;
import com.amideljuyi.thesisportal.domain.Supervisor;
import java.util.List;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Supervisor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor,Long> {
    List<Supervisor> findByProfessor(Professor professor);
    
}
