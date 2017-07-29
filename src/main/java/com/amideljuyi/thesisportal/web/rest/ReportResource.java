package com.amideljuyi.thesisportal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amideljuyi.thesisportal.domain.Professor;
import com.amideljuyi.thesisportal.domain.Student;
import com.amideljuyi.thesisportal.domain.Supervisor;
import com.amideljuyi.thesisportal.domain.enumeration.Status;
import com.amideljuyi.thesisportal.repository.ProfessorRepository;
import com.amideljuyi.thesisportal.repository.StudentRepository;
import com.amideljuyi.thesisportal.repository.SupervisorRepository;
import com.amideljuyi.thesisportal.repository.search.ProfessorSearchRepository;
import com.amideljuyi.thesisportal.repository.search.StudentSearchRepository;
import com.amideljuyi.thesisportal.repository.search.SupervisorSearchRepository;
import com.amideljuyi.thesisportal.web.rest.errors.ErrorVM;
import com.amideljuyi.thesisportal.web.rest.util.HeaderUtil;
import com.amideljuyi.thesisportal.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Supervisor.
 */
@RestController
@RequestMapping("/report")
public class ReportResource {

    private static final String ENTITY_NAME = "report";

    private final SupervisorRepository supervisorRepository;

    private final SupervisorSearchRepository supervisorSearchRepository;

    private final StudentRepository studentRepository;

    private final StudentSearchRepository studentSearchRepository;

    private final ProfessorRepository professorRepository;

    private final ProfessorSearchRepository professorSearchRepository;

    public ReportResource(SupervisorRepository supervisorRepository,
            SupervisorSearchRepository supervisorSearchRepository, StudentRepository studentRepository,
            StudentSearchRepository studentSearchRepository, ProfessorRepository professorRepository,
            ProfessorSearchRepository professorSearchRepository) {
        this.professorRepository = professorRepository;
        this.professorSearchRepository = professorSearchRepository;
        this.studentRepository = studentRepository;
        this.studentSearchRepository = studentSearchRepository;
        this.supervisorRepository = supervisorRepository;
        this.supervisorSearchRepository = supervisorSearchRepository;
    }

    /**
     * POST  /supervisors : Create a new supervisor.
     *
     * @param supervisor the supervisor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new supervisor, or with status 400 (Bad Request) if the supervisor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    /**
     * GET  /supervisors : get all the supervisors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of supervisors in body
     */

    @GetMapping(path = "/supervisors", params = { "fromDate", "toDate" , "id" })
    @Timed
    public ResponseEntity<List<Supervisor>> getAllSupervisorsByDates(
            @RequestParam(value = "fromDate") LocalDate fromDate, @RequestParam(value = "toDate") LocalDate toDate,
            @RequestParam(value = "id") Long id, @ApiParam Pageable pageable) {

        Professor professor = professorRepository.findOne(id);

        Page<Supervisor> page = supervisorRepository.findByProfessorAndStartTimeBetween(professor,
                fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant(), pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/report/supervisors/" + id);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /supervisors/:id : get the "id" supervisor.
     *
     * @param id the id of the supervisor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the supervisor, or with status 404 (Not Found)
     */
    @GetMapping("/supervisors/{id}")
    @Timed
    public ResponseEntity<List<Supervisor>> getAllSupervisors(@PathVariable Long id, @ApiParam Pageable pageable) {

        Professor professor = professorRepository.findOne(id);

        Page<Supervisor> page = supervisorRepository.findByProfessor(professor, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/report/supervisors/" + id);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
