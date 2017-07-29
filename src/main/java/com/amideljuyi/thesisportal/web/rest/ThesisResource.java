package com.amideljuyi.thesisportal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amideljuyi.thesisportal.domain.Professor;
import com.amideljuyi.thesisportal.domain.Supervisor;
import com.amideljuyi.thesisportal.domain.Thesis;
import com.amideljuyi.thesisportal.repository.ProfessorRepository;
import com.amideljuyi.thesisportal.repository.SupervisorRepository;
import com.amideljuyi.thesisportal.repository.ThesisRepository;
import com.amideljuyi.thesisportal.repository.search.ThesisSearchRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Thesis.
 */
@RestController
@RequestMapping("/api")
public class ThesisResource {

    private final Logger log = LoggerFactory.getLogger(ThesisResource.class);

    private static final String ENTITY_NAME = "thesis";

    private final ThesisRepository thesisRepository;

    private final ThesisSearchRepository thesisSearchRepository;

    private final SupervisorRepository supervisorRepository;

    private final ProfessorRepository professorRepository;

    public ThesisResource(ThesisRepository thesisRepository, ThesisSearchRepository thesisSearchRepository,SupervisorRepository supervisorRepository,ProfessorRepository professorRepository) {
        this.professorRepository=professorRepository;
        this.supervisorRepository=supervisorRepository;
        this.thesisRepository = thesisRepository;
        this.thesisSearchRepository = thesisSearchRepository;
    }

    /**
     * POST  /theses : Create a new thesis.
     *
     * @param thesis the thesis to create
     * @return the ResponseEntity with status 201 (Created) and with body the new thesis, or with status 400 (Bad Request) if the thesis has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/theses")
    @Timed
    public ResponseEntity<Thesis> createThesis(@Valid @RequestBody Thesis thesis) throws URISyntaxException {
        log.debug("REST request to save Thesis : {}", thesis);
        if (thesis.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new thesis cannot already have an ID")).body(null);
        }
        thesis.setNumOfReferee(0);
        Thesis result = thesisRepository.save(thesis);
        thesisSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/theses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /theses : Updates an existing thesis.
     *
     * @param thesis the thesis to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated thesis,
     * or with status 400 (Bad Request) if the thesis is not valid,
     * or with status 500 (Internal Server Error) if the thesis couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/theses")
    @Timed
    public ResponseEntity<Thesis> updateThesis(@Valid @RequestBody Thesis thesis) throws URISyntaxException {
        log.debug("REST request to update Thesis : {}", thesis);
        if (thesis.getId() == null) {
            return createThesis(thesis);
        }
        Thesis result = thesisRepository.save(thesis);
        thesisSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, thesis.getId().toString()))
            .body(result);
    }

    /**
     * GET  /theses : get all the theses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of theses in body
     */
    @GetMapping("/theses")
    @Timed
    public ResponseEntity<List<Thesis>> getAllTheses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Theses");
        Page<Thesis> page = thesisRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/theses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /theses/:id : get the "id" thesis.
     *
     * @param id the id of the thesis to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the thesis, or with status 404 (Not Found)
     */
    @GetMapping("/theses/{id}")
    @Timed
    public ResponseEntity<Thesis> getThesis(@PathVariable Long id) {
        log.debug("REST request to get Thesis : {}", id);
        Thesis thesis = thesisRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(thesis));
    }

    /**
     * DELETE  /theses/:id : delete the "id" thesis.
     *
     * @param id the id of the thesis to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/theses/{id}")
    @Timed
    public ResponseEntity<Void> deleteThesis(@PathVariable Long id) {
        log.debug("REST request to delete Thesis : {}", id);
        thesisRepository.delete(id);
        thesisSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/theses?query=:query : search for the thesis corresponding
     * to the query.
     *
     * @param query the query of the thesis search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/theses")
    @Timed
    public ResponseEntity<List<Thesis>> searchTheses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Theses for query {}", query);
        Page<Thesis> page = thesisSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/theses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/theses/professor/{id}")
    @Timed
    public ResponseEntity<List<Thesis>> getAllThesesByProfessor(@PathVariable Long id, @ApiParam Pageable pageable) {

        Professor professor = professorRepository.findOne(id);

        Page<Supervisor> page = supervisorRepository.findByProfessor(professor, pageable);
        List<Thesis> theses=new ArrayList<Thesis>();
        for(Supervisor item:page.getContent()){
            if(item.getStudent().getThesis()!=null)
                theses.add(item.getStudent().getThesis());
        }
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/theses/professor/" + id);
        return new ResponseEntity<>(theses, headers, HttpStatus.OK);
    }
}
