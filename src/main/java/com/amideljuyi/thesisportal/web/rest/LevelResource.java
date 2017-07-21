package com.amideljuyi.thesisportal.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amideljuyi.thesisportal.domain.Level;

import com.amideljuyi.thesisportal.repository.LevelRepository;
import com.amideljuyi.thesisportal.repository.search.LevelSearchRepository;
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
 * REST controller for managing Level.
 */
@RestController
@RequestMapping("/api")
public class LevelResource {

    private final Logger log = LoggerFactory.getLogger(LevelResource.class);

    private static final String ENTITY_NAME = "level";

    private final LevelRepository levelRepository;

    private final LevelSearchRepository levelSearchRepository;

    public LevelResource(LevelRepository levelRepository, LevelSearchRepository levelSearchRepository) {
        this.levelRepository = levelRepository;
        this.levelSearchRepository = levelSearchRepository;
    }

    /**
     * POST  /levels : Create a new level.
     *
     * @param level the level to create
     * @return the ResponseEntity with status 201 (Created) and with body the new level, or with status 400 (Bad Request) if the level has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/levels")
    @Timed
    public ResponseEntity<Level> createLevel(@Valid @RequestBody Level level) throws URISyntaxException {
        log.debug("REST request to save Level : {}", level);
        if (level.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new level cannot already have an ID")).body(null);
        }
        Level result = levelRepository.save(level);
        levelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /levels : Updates an existing level.
     *
     * @param level the level to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated level,
     * or with status 400 (Bad Request) if the level is not valid,
     * or with status 500 (Internal Server Error) if the level couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/levels")
    @Timed
    public ResponseEntity<Level> updateLevel(@Valid @RequestBody Level level) throws URISyntaxException {
        log.debug("REST request to update Level : {}", level);
        if (level.getId() == null) {
            return createLevel(level);
        }
        Level result = levelRepository.save(level);
        levelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, level.getId().toString()))
            .body(result);
    }

    /**
     * GET  /levels : get all the levels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of levels in body
     */
    @GetMapping("/levels")
    @Timed
    public ResponseEntity<List<Level>> getAllLevels(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Levels");
        Page<Level> page = levelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /levels/:id : get the "id" level.
     *
     * @param id the id of the level to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the level, or with status 404 (Not Found)
     */
    @GetMapping("/levels/{id}")
    @Timed
    public ResponseEntity<Level> getLevel(@PathVariable Long id) {
        log.debug("REST request to get Level : {}", id);
        Level level = levelRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(level));
    }

    /**
     * DELETE  /levels/:id : delete the "id" level.
     *
     * @param id the id of the level to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/levels/{id}")
    @Timed
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        log.debug("REST request to delete Level : {}", id);
        levelRepository.delete(id);
        levelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/levels?query=:query : search for the level corresponding
     * to the query.
     *
     * @param query the query of the level search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/levels")
    @Timed
    public ResponseEntity<List<Level>> searchLevels(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Levels for query {}", query);
        Page<Level> page = levelSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
