package com.amideljuyi.thesisportal.web.rest;

import com.amideljuyi.thesisportal.ThesisPortalApp;

import com.amideljuyi.thesisportal.domain.Adviser;
import com.amideljuyi.thesisportal.repository.AdviserRepository;
import com.amideljuyi.thesisportal.repository.search.AdviserSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AdviserResource REST controller.
 *
 * @see AdviserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisPortalApp.class)
public class AdviserResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AdviserRepository adviserRepository;

    @Autowired
    private AdviserSearchRepository adviserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdviserMockMvc;

    private Adviser adviser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdviserResource adviserResource = new AdviserResource(adviserRepository, adviserSearchRepository);
        this.restAdviserMockMvc = MockMvcBuilders.standaloneSetup(adviserResource)
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
    public static Adviser createEntity(EntityManager em) {
        Adviser adviser = new Adviser()
            .name(DEFAULT_NAME)
            .startTime(DEFAULT_START_TIME);
        return adviser;
    }

    @Before
    public void initTest() {
        adviserSearchRepository.deleteAll();
        adviser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdviser() throws Exception {
        int databaseSizeBeforeCreate = adviserRepository.findAll().size();

        // Create the Adviser
        restAdviserMockMvc.perform(post("/api/advisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adviser)))
            .andExpect(status().isCreated());

        // Validate the Adviser in the database
        List<Adviser> adviserList = adviserRepository.findAll();
        assertThat(adviserList).hasSize(databaseSizeBeforeCreate + 1);
        Adviser testAdviser = adviserList.get(adviserList.size() - 1);
        assertThat(testAdviser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAdviser.getStartTime()).isEqualTo(DEFAULT_START_TIME);

        // Validate the Adviser in Elasticsearch
        Adviser adviserEs = adviserSearchRepository.findOne(testAdviser.getId());
        assertThat(adviserEs).isEqualToComparingFieldByField(testAdviser);
    }

    @Test
    @Transactional
    public void createAdviserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adviserRepository.findAll().size();

        // Create the Adviser with an existing ID
        adviser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdviserMockMvc.perform(post("/api/advisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adviser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Adviser> adviserList = adviserRepository.findAll();
        assertThat(adviserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = adviserRepository.findAll().size();
        // set the field null
        adviser.setStartTime(null);

        // Create the Adviser, which fails.

        restAdviserMockMvc.perform(post("/api/advisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adviser)))
            .andExpect(status().isBadRequest());

        List<Adviser> adviserList = adviserRepository.findAll();
        assertThat(adviserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdvisers() throws Exception {
        // Initialize the database
        adviserRepository.saveAndFlush(adviser);

        // Get all the adviserList
        restAdviserMockMvc.perform(get("/api/advisers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adviser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())));
    }

    @Test
    @Transactional
    public void getAdviser() throws Exception {
        // Initialize the database
        adviserRepository.saveAndFlush(adviser);

        // Get the adviser
        restAdviserMockMvc.perform(get("/api/advisers/{id}", adviser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adviser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdviser() throws Exception {
        // Get the adviser
        restAdviserMockMvc.perform(get("/api/advisers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdviser() throws Exception {
        // Initialize the database
        adviserRepository.saveAndFlush(adviser);
        adviserSearchRepository.save(adviser);
        int databaseSizeBeforeUpdate = adviserRepository.findAll().size();

        // Update the adviser
        Adviser updatedAdviser = adviserRepository.findOne(adviser.getId());
        updatedAdviser
            .name(UPDATED_NAME)
            .startTime(UPDATED_START_TIME);

        restAdviserMockMvc.perform(put("/api/advisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdviser)))
            .andExpect(status().isOk());

        // Validate the Adviser in the database
        List<Adviser> adviserList = adviserRepository.findAll();
        assertThat(adviserList).hasSize(databaseSizeBeforeUpdate);
        Adviser testAdviser = adviserList.get(adviserList.size() - 1);
        assertThat(testAdviser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdviser.getStartTime()).isEqualTo(UPDATED_START_TIME);

        // Validate the Adviser in Elasticsearch
        Adviser adviserEs = adviserSearchRepository.findOne(testAdviser.getId());
        assertThat(adviserEs).isEqualToComparingFieldByField(testAdviser);
    }

    @Test
    @Transactional
    public void updateNonExistingAdviser() throws Exception {
        int databaseSizeBeforeUpdate = adviserRepository.findAll().size();

        // Create the Adviser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdviserMockMvc.perform(put("/api/advisers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adviser)))
            .andExpect(status().isCreated());

        // Validate the Adviser in the database
        List<Adviser> adviserList = adviserRepository.findAll();
        assertThat(adviserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdviser() throws Exception {
        // Initialize the database
        adviserRepository.saveAndFlush(adviser);
        adviserSearchRepository.save(adviser);
        int databaseSizeBeforeDelete = adviserRepository.findAll().size();

        // Get the adviser
        restAdviserMockMvc.perform(delete("/api/advisers/{id}", adviser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean adviserExistsInEs = adviserSearchRepository.exists(adviser.getId());
        assertThat(adviserExistsInEs).isFalse();

        // Validate the database is empty
        List<Adviser> adviserList = adviserRepository.findAll();
        assertThat(adviserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdviser() throws Exception {
        // Initialize the database
        adviserRepository.saveAndFlush(adviser);
        adviserSearchRepository.save(adviser);

        // Search the adviser
        restAdviserMockMvc.perform(get("/api/_search/advisers?query=id:" + adviser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adviser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adviser.class);
        Adviser adviser1 = new Adviser();
        adviser1.setId(1L);
        Adviser adviser2 = new Adviser();
        adviser2.setId(adviser1.getId());
        assertThat(adviser1).isEqualTo(adviser2);
        adviser2.setId(2L);
        assertThat(adviser1).isNotEqualTo(adviser2);
        adviser1.setId(null);
        assertThat(adviser1).isNotEqualTo(adviser2);
    }
}
