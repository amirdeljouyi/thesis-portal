package com.amideljuyi.thesisportal.web.rest;

import com.amideljuyi.thesisportal.ThesisPortalApp;

import com.amideljuyi.thesisportal.domain.Referee;
import com.amideljuyi.thesisportal.repository.RefereeRepository;
import com.amideljuyi.thesisportal.repository.search.RefereeSearchRepository;
import com.amideljuyi.thesisportal.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RefereeResource REST controller.
 *
 * @see RefereeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisPortalApp.class)
public class RefereeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RefereeRepository refereeRepository;

    @Autowired
    private RefereeSearchRepository refereeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRefereeMockMvc;

    private Referee referee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RefereeResource refereeResource = new RefereeResource(refereeRepository, refereeSearchRepository);
        this.restRefereeMockMvc = MockMvcBuilders.standaloneSetup(refereeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referee createEntity(EntityManager em) {
        Referee referee = new Referee()
            .name(DEFAULT_NAME);
        return referee;
    }

    @Before
    public void initTest() {
        refereeSearchRepository.deleteAll();
        referee = createEntity(em);
    }

    @Test
    @Transactional
    public void createReferee() throws Exception {
        int databaseSizeBeforeCreate = refereeRepository.findAll().size();

        // Create the Referee
        restRefereeMockMvc.perform(post("/api/referees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(referee)))
            .andExpect(status().isCreated());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeCreate + 1);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Referee in Elasticsearch
        Referee refereeEs = refereeSearchRepository.findOne(testReferee.getId());
        assertThat(refereeEs).isEqualToComparingFieldByField(testReferee);
    }

    @Test
    @Transactional
    public void createRefereeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = refereeRepository.findAll().size();

        // Create the Referee with an existing ID
        referee.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefereeMockMvc.perform(post("/api/referees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(referee)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReferees() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get all the refereeList
        restRefereeMockMvc.perform(get("/api/referees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get the referee
        restRefereeMockMvc.perform(get("/api/referees/{id}", referee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(referee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReferee() throws Exception {
        // Get the referee
        restRefereeMockMvc.perform(get("/api/referees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);
        refereeSearchRepository.save(referee);
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();

        // Update the referee
        Referee updatedReferee = refereeRepository.findOne(referee.getId());
        updatedReferee
            .name(UPDATED_NAME);

        restRefereeMockMvc.perform(put("/api/referees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReferee)))
            .andExpect(status().isOk());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Referee in Elasticsearch
        Referee refereeEs = refereeSearchRepository.findOne(testReferee.getId());
        assertThat(refereeEs).isEqualToComparingFieldByField(testReferee);
    }

    @Test
    @Transactional
    public void updateNonExistingReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();

        // Create the Referee

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRefereeMockMvc.perform(put("/api/referees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(referee)))
            .andExpect(status().isCreated());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);
        refereeSearchRepository.save(referee);
        int databaseSizeBeforeDelete = refereeRepository.findAll().size();

        // Get the referee
        restRefereeMockMvc.perform(delete("/api/referees/{id}", referee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean refereeExistsInEs = refereeSearchRepository.exists(referee.getId());
        assertThat(refereeExistsInEs).isFalse();

        // Validate the database is empty
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);
        refereeSearchRepository.save(referee);

        // Search the referee
        restRefereeMockMvc.perform(get("/api/_search/referees?query=id:" + referee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Referee.class);
        Referee referee1 = new Referee();
        referee1.setId(1L);
        Referee referee2 = new Referee();
        referee2.setId(referee1.getId());
        assertThat(referee1).isEqualTo(referee2);
        referee2.setId(2L);
        assertThat(referee1).isNotEqualTo(referee2);
        referee1.setId(null);
        assertThat(referee1).isNotEqualTo(referee2);
    }
}
