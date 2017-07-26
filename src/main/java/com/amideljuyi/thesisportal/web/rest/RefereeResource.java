package com.amideljuyi.thesisportal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amideljuyi.thesisportal.domain.Referee;
import com.amideljuyi.thesisportal.domain.Thesis;
import com.amideljuyi.thesisportal.repository.RefereeRepository;
import com.amideljuyi.thesisportal.repository.ThesisRepository;
import com.amideljuyi.thesisportal.repository.search.RefereeSearchRepository;
import com.amideljuyi.thesisportal.repository.search.ThesisSearchRepository;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Referee.
 */
@RestController
@RequestMapping("/api")
public class RefereeResource {

    private final Logger log = LoggerFactory.getLogger(RefereeResource.class);

    private static final String ENTITY_NAME = "referee";

    private final RefereeRepository refereeRepository;

    private final RefereeSearchRepository refereeSearchRepository;

    private final ThesisRepository thesisRepository;

    private final ThesisSearchRepository thesisSearchRepository;

    public RefereeResource(RefereeRepository refereeRepository, RefereeSearchRepository refereeSearchRepository,
            ThesisRepository thesisRepository, ThesisSearchRepository thesisSearchRepository) {
        this.thesisRepository = thesisRepository;
        this.thesisSearchRepository = thesisSearchRepository;
        this.refereeRepository = refereeRepository;
        this.refereeSearchRepository = refereeSearchRepository;
    }

    /**
     * POST  /referees : Create a new referee.
     *
     * @param referee the referee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new referee, or with status 400 (Bad Request) if the referee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/referees")
    @Timed
    public ResponseEntity<Referee> createReferee(@RequestBody Referee referee) throws URISyntaxException {
        referee.setName(referee.getProfessor().getName() + "-" + referee.getThesis().getTitle());

        log.debug("REST request to save Referee : {}", referee);
        if (referee.getId() != null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new referee cannot already have an ID"))
                    .body(null);
        }

        ErrorVM validationError = validation(referee.getThesis());

        if (validationError != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
                    validationError.getMessage(), validationError.getDescription())).body(null);
        
        updateThesis(referee.getThesis(),+1);

        Referee result = refereeRepository.save(referee);
        refereeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/referees/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT  /referees : Updates an existing referee.
     *
     * @param referee the referee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated referee,
     * or with status 400 (Bad Request) if the referee is not valid,
     * or with status 500 (Internal Server Error) if the referee couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/referees")
    @Timed
    public ResponseEntity<Referee> updateReferee(@RequestBody Referee referee) throws URISyntaxException {
        log.debug("REST request to update Referee : {}", referee);
        if (referee.getId() == null) {
            return createReferee(referee);
        }

        Referee lastThesis = refereeRepository.findOne(referee.getId());
        boolean isThesisChanged = !lastThesis.getThesis().equals(referee.getThesis());
        ErrorVM validationError = isThesisChanged ? validation(referee.getThesis()) : null;
        
        if (validationError != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
                    validationError.getMessage(), validationError.getDescription())).body(null);
        if(isThesisChanged){
            updateThesis(referee.getThesis(), +1);
            updateThesis(lastThesis.getThesis(), -1);
        }

        Referee result = refereeRepository.save(referee);
        refereeSearchRepository.save(result);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, referee.getId().toString()))
                .body(result);
    }

    /**
     * GET  /referees : get all the referees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of referees in body
     */
    @GetMapping("/referees")
    @Timed
    public ResponseEntity<List<Referee>> getAllReferees(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Referees");
        Page<Referee> page = refereeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/referees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /referees/:id : get the "id" referee.
     *
     * @param id the id of the referee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the referee, or with status 404 (Not Found)
     */
    @GetMapping("/referees/{id}")
    @Timed
    public ResponseEntity<Referee> getReferee(@PathVariable Long id) {
        log.debug("REST request to get Referee : {}", id);
        Referee referee = refereeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(referee));
    }

    /**
     * DELETE  /referees/:id : delete the "id" referee.
     *
     * @param id the id of the referee to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/referees/{id}")
    @Timed
    public ResponseEntity<Void> deleteReferee(@PathVariable Long id) {
        log.debug("REST request to delete Referee : {}", id);
        // update thesis
        updateThesis(refereeRepository.findOne(id).getThesis() , -1);
        refereeRepository.delete(id);
        refereeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/referees?query=:query : search for the referee corresponding
     * to the query.
     *
     * @param query the query of the referee search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/referees")
    @Timed
    public ResponseEntity<List<Referee>> searchReferees(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Referees for query {}", query);
        Page<Referee> page = refereeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/referees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private void updateThesis(Thesis thesis,int value){
        thesis.setNumOfReferee(thesis.getNumOfReferee() + value);
        thesisRepository.save(thesis);
        thesisSearchRepository.save(thesis);
    }
    private ErrorVM validation(Thesis thesis){
        if(thesis.getNumOfReferee()>1)
            return new ErrorVM( "thesishaveenoughreferees", "Thesis have enough referees");
        else return null;
    }

}
