package com.amideljuyi.thesisportal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amideljuyi.thesisportal.domain.Adviser;
import com.amideljuyi.thesisportal.domain.Student;
import com.amideljuyi.thesisportal.repository.AdviserRepository;
import com.amideljuyi.thesisportal.repository.StudentRepository;
import com.amideljuyi.thesisportal.repository.search.AdviserSearchRepository;
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
 * REST controller for managing Adviser.
 */
@RestController
@RequestMapping("/api")
public class AdviserResource {

    private final Logger log = LoggerFactory.getLogger(AdviserResource.class);

    private static final String ENTITY_NAME = "adviser";

    private final AdviserRepository adviserRepository;

    private final AdviserSearchRepository adviserSearchRepository;

    private final StudentRepository studentRepository;

    private final StudentSearchRepository studentSearchRepository;

    public AdviserResource(AdviserRepository adviserRepository, AdviserSearchRepository adviserSearchRepository,StudentRepository studentRepository, StudentSearchRepository studentSearchRepository) {
        this.studentRepository = studentRepository;
        this.studentSearchRepository = studentSearchRepository;
        this.adviserRepository = adviserRepository;
        this.adviserSearchRepository = adviserSearchRepository;
    }

    /**
     * POST  /advisers : Create a new adviser.
     *
     * @param adviser the adviser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adviser, or with status 400 (Bad Request) if the adviser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/advisers")
    @Timed
    public ResponseEntity<Adviser> createAdviser(@Valid @RequestBody Adviser adviser) throws URISyntaxException {
        log.debug("REST request to save Adviser : {}", adviser);
        if (adviser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new adviser cannot already have an ID")).body(null);
        }
        Student student =adviser.getStudent();
        student.setNumOfAdviser(student.getNumOfAdviser()+1);
        studentRepository.save(student);
        studentSearchRepository.save(student);

        Adviser result = adviserRepository.save(adviser);
        adviserSearchRepository.save(result);
        
        return ResponseEntity.created(new URI("/api/advisers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /advisers : Updates an existing adviser.
     *
     * @param adviser the adviser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adviser,
     * or with status 400 (Bad Request) if the adviser is not valid,
     * or with status 500 (Internal Server Error) if the adviser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/advisers")
    @Timed
    public ResponseEntity<Adviser> updateAdviser(@Valid @RequestBody Adviser adviser) throws URISyntaxException {
        log.debug("REST request to update Adviser : {}", adviser);
        if (adviser.getId() == null) {
            return createAdviser(adviser);
        }
        Adviser result = adviserRepository.save(adviser);
        adviserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adviser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /advisers : get all the advisers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of advisers in body
     */
    @GetMapping("/advisers")
    @Timed
    public ResponseEntity<List<Adviser>> getAllAdvisers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Advisers");
        Page<Adviser> page = adviserRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/advisers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /advisers/:id : get the "id" adviser.
     *
     * @param id the id of the adviser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adviser, or with status 404 (Not Found)
     */
    @GetMapping("/advisers/{id}")
    @Timed
    public ResponseEntity<Adviser> getAdviser(@PathVariable Long id) {
        log.debug("REST request to get Adviser : {}", id);
        Adviser adviser = adviserRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(adviser));
    }

    /**
     * DELETE  /advisers/:id : delete the "id" adviser.
     *
     * @param id the id of the adviser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/advisers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdviser(@PathVariable Long id) {
        log.debug("REST request to delete Adviser : {}", id);
        adviserRepository.delete(id);
        adviserSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/advisers?query=:query : search for the adviser corresponding
     * to the query.
     *
     * @param query the query of the adviser search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/advisers")
    @Timed
    public ResponseEntity<List<Adviser>> searchAdvisers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Advisers for query {}", query);
        Page<Adviser> page = adviserSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/advisers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
