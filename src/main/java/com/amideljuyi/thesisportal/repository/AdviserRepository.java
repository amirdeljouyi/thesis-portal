package com.amideljuyi.thesisportal.repository;

import com.amideljuyi.thesisportal.domain.Adviser;
import com.amideljuyi.thesisportal.domain.Professor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Adviser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdviserRepository extends JpaRepository<Adviser,Long> {
    Page<Adviser> findByProfessor(Professor professor, Pageable pageable);
}
