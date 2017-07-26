package com.amideljuyi.thesisportal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amideljuyi.thesisportal.domain.Professor;
import com.amideljuyi.thesisportal.domain.Student;
import com.amideljuyi.thesisportal.domain.enumeration.Status;
import com.amideljuyi.thesisportal.repository.ProfessorRepository;
import com.amideljuyi.thesisportal.repository.StudentRepository;
import com.amideljuyi.thesisportal.repository.search.ProfessorSearchRepository;
import com.amideljuyi.thesisportal.repository.search.StudentSearchRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Student.
 */
@RestController
@RequestMapping("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private static final String ENTITY_NAME = "student";

    private final StudentRepository studentRepository;

    private final StudentSearchRepository studentSearchRepository;

    private final ProfessorRepository professorRepository;

    private final ProfessorSearchRepository professorSearchRepository;

    public StudentResource(StudentRepository studentRepository, StudentSearchRepository studentSearchRepository,
            ProfessorRepository professorRepository, ProfessorSearchRepository professorSearchRepository) {
        this.professorRepository = professorRepository;
        this.professorSearchRepository = professorSearchRepository;
        this.studentRepository = studentRepository;
        this.studentSearchRepository = studentSearchRepository;
    }

    /**
     * POST  /students : Create a new student.
     *
     * @param student the student to create
     * @return the ResponseEntity with status 201 (Created) and with body the new student, or with status 400 (Bad Request) if the student has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/students")
    @Timed
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) throws URISyntaxException {
        log.debug("REST request to save Student : {}", student);
        if (student.getId() != null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new student cannot already have an ID"))
                    .body(null);
        }
        Student result = studentRepository.save(student);
        studentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/students/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT  /students : Updates an existing student.
     *
     * @param student the student to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated student,
     * or with status 400 (Bad Request) if the student is not valid,
     * or with status 500 (Internal Server Error) if the student couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/students")
    @Timed
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody Student student) throws URISyntaxException {
        log.debug("REST request to update Student : {}", student);
        if (student.getId() == null) {
            return createStudent(student);
        }

        Student lastStudent = studentRepository.findOne(student.getId());

        if (lastStudent.getStatus() == Status.INPRORGESS && student.getStatus() != Status.INPRORGESS) {
            if (student.getNumOfSupervisor() == 2)
                student.getSupervisers().forEach((v) -> updateProfessor(v.getProfessor(), +1));
            else
                student.getSupervisers().forEach((v) -> updateProfessor(v.getProfessor(), +2));
        } else if (lastStudent.getStatus() != Status.INPRORGESS && student.getStatus() == Status.INPRORGESS) {
            // validation required
            if (student.getNumOfSupervisor() == 2)
                student.getSupervisers().forEach((v) -> updateProfessor(v.getProfessor(), -1));
            else
                student.getSupervisers().forEach((v) -> updateProfessor(v.getProfessor(), -2));
        }

        Student result = studentRepository.save(student);
        studentSearchRepository.save(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, student.getId().toString()))
                .body(result);
    }

    /**
     * GET  /students : get all the students.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of students in body
     */
    @GetMapping("/students")
    @Timed
    public ResponseEntity<List<Student>> getAllStudents(@ApiParam Pageable pageable,
            @RequestParam(required = false) String filter) {
        if ("thesis-is-null".equals(filter)) {
            log.debug("REST request to get all Students where thesis is null");
            return new ResponseEntity<>(
                    StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                            .filter(student -> student.getThesis() == null).collect(Collectors.toList()),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Students");
        Page<Student> page = studentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /students/:id : get the "id" student.
     *
     * @param id the id of the student to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the student, or with status 404 (Not Found)
     */
    @GetMapping("/students/{id}")
    @Timed
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        log.debug("REST request to get Student : {}", id);
        Student student = studentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(student));
    }

    /**
     * DELETE  /students/:id : delete the "id" student.
     *
     * @param id the id of the student to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/students/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.debug("REST request to delete Student : {}", id);
        studentRepository.delete(id);
        studentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/students?query=:query : search for the student corresponding
     * to the query.
     *
     * @param query the query of the student search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/students")
    @Timed
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Students for query {}", query);
        Page<Student> page = studentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private void updateProfessor(Professor professor, int value) {
        professor.setFreeCapacityOfTotal(professor.getFreeCapacityOfTotal() + value);
        professorRepository.save(professor);
        professorSearchRepository.save(professor);
    }

}
