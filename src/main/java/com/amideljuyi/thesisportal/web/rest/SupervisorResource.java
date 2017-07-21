package com.amideljuyi.thesisportal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amideljuyi.thesisportal.domain.Supervisor;

import com.amideljuyi.thesisportal.repository.SupervisorRepository;
import com.amideljuyi.thesisportal.repository.search.SupervisorSearchRepository;
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
 * REST controller for managing Supervisor.
 */
@RestController
@RequestMapping("/api")
public class SupervisorResource {

    private final Logger log = LoggerFactory.getLogger(SupervisorResource.class);

    private static final String ENTITY_NAME = "supervisor";

    private final SupervisorRepository supervisorRepository;

    private final SupervisorSearchRepository supervisorSearchRepository;

    public SupervisorResource(SupervisorRepository supervisorRepository, SupervisorSearchRepository supervisorSearchRepository) {
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
    @PostMapping("/supervisors")
    @Timed
    public ResponseEntity<Supervisor> createSupervisor(@Valid @RequestBody Supervisor supervisor) throws URISyntaxException {
        log.debug("REST request to save Supervisor : {}", supervisor);
        if (supervisor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new supervisor cannot already have an ID")).body(null);
        }
        Supervisor result = supervisorRepository.save(supervisor);
        supervisorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/supervisors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /supervisors : Updates an existing supervisor.
     *
     * @param supervisor the supervisor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated supervisor,
     * or with status 400 (Bad Request) if the supervisor is not valid,
     * or with status 500 (Internal Server Error) if the supervisor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/supervisors")
    @Timed
    public ResponseEntity<Supervisor> updateSupervisor(@Valid @RequestBody Supervisor supervisor) throws URISyntaxException {
        log.debug("REST request to update Supervisor : {}", supervisor);
        if (supervisor.getId() == null) {
            return createSupervisor(supervisor);
        }
        Supervisor result = supervisorRepository.save(supervisor);
        supervisorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, supervisor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /supervisors : get all the supervisors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of supervisors in body
     */
    @GetMapping("/supervisors")
    @Timed
    public ResponseEntity<List<Supervisor>> getAllSupervisors(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Supervisors");
        Page<Supervisor> page = supervisorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/supervisors");
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
    public ResponseEntity<Supervisor> getSupervisor(@PathVariable Long id) {
        log.debug("REST request to get Supervisor : {}", id);
        Supervisor supervisor = supervisorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(supervisor));
    }

    /**
     * DELETE  /supervisors/:id : delete the "id" supervisor.
     *
     * @param id the id of the supervisor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/supervisors/{id}")
    @Timed
    public ResponseEntity<Void> deleteSupervisor(@PathVariable Long id) {
        log.debug("REST request to delete Supervisor : {}", id);
        supervisorRepository.delete(id);
        supervisorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/supervisors?query=:query : search for the supervisor corresponding
     * to the query.
     *
     * @param query the query of the supervisor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/supervisors")
    @Timed
    public ResponseEntity<List<Supervisor>> searchSupervisors(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Supervisors for query {}", query);
        Page<Supervisor> page = supervisorSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/supervisors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
