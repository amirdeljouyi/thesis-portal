package com.amideljuyi.thesisportal.web.rest;

import com.amideljuyi.thesisportal.ThesisPortalApp;

import com.amideljuyi.thesisportal.domain.Supervisor;
import com.amideljuyi.thesisportal.repository.SupervisorRepository;
import com.amideljuyi.thesisportal.repository.search.SupervisorSearchRepository;
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
 * Test class for the SupervisorResource REST controller.
 *
 * @see SupervisorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisPortalApp.class)
public class SupervisorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private SupervisorSearchRepository supervisorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSupervisorMockMvc;

    private Supervisor supervisor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupervisorResource supervisorResource = new SupervisorResource(supervisorRepository, supervisorSearchRepository);
        this.restSupervisorMockMvc = MockMvcBuilders.standaloneSetup(supervisorResource)
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
    public static Supervisor createEntity(EntityManager em) {
        Supervisor supervisor = new Supervisor()
            .name(DEFAULT_NAME)
            .startTime(DEFAULT_START_TIME);
        return supervisor;
    }

    @Before
    public void initTest() {
        supervisorSearchRepository.deleteAll();
        supervisor = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupervisor() throws Exception {
        int databaseSizeBeforeCreate = supervisorRepository.findAll().size();

        // Create the Supervisor
        restSupervisorMockMvc.perform(post("/api/supervisors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supervisor)))
            .andExpect(status().isCreated());

        // Validate the Supervisor in the database
        List<Supervisor> supervisorList = supervisorRepository.findAll();
        assertThat(supervisorList).hasSize(databaseSizeBeforeCreate + 1);
        Supervisor testSupervisor = supervisorList.get(supervisorList.size() - 1);
        assertThat(testSupervisor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupervisor.getStartTime()).isEqualTo(DEFAULT_START_TIME);

        // Validate the Supervisor in Elasticsearch
        Supervisor supervisorEs = supervisorSearchRepository.findOne(testSupervisor.getId());
        assertThat(supervisorEs).isEqualToComparingFieldByField(testSupervisor);
    }

    @Test
    @Transactional
    public void createSupervisorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supervisorRepository.findAll().size();

        // Create the Supervisor with an existing ID
        supervisor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupervisorMockMvc.perform(post("/api/supervisors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supervisor)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Supervisor> supervisorList = supervisorRepository.findAll();
        assertThat(supervisorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = supervisorRepository.findAll().size();
        // set the field null
        supervisor.setStartTime(null);

        // Create the Supervisor, which fails.

        restSupervisorMockMvc.perform(post("/api/supervisors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supervisor)))
            .andExpect(status().isBadRequest());

        List<Supervisor> supervisorList = supervisorRepository.findAll();
        assertThat(supervisorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSupervisors() throws Exception {
        // Initialize the database
        supervisorRepository.saveAndFlush(supervisor);

        // Get all the supervisorList
        restSupervisorMockMvc.perform(get("/api/supervisors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supervisor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())));
    }

    @Test
    @Transactional
    public void getSupervisor() throws Exception {
        // Initialize the database
        supervisorRepository.saveAndFlush(supervisor);

        // Get the supervisor
        restSupervisorMockMvc.perform(get("/api/supervisors/{id}", supervisor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supervisor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSupervisor() throws Exception {
        // Get the supervisor
        restSupervisorMockMvc.perform(get("/api/supervisors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupervisor() throws Exception {
        // Initialize the database
        supervisorRepository.saveAndFlush(supervisor);
        supervisorSearchRepository.save(supervisor);
        int databaseSizeBeforeUpdate = supervisorRepository.findAll().size();

        // Update the supervisor
        Supervisor updatedSupervisor = supervisorRepository.findOne(supervisor.getId());
        updatedSupervisor
            .name(UPDATED_NAME)
            .startTime(UPDATED_START_TIME);

        restSupervisorMockMvc.perform(put("/api/supervisors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSupervisor)))
            .andExpect(status().isOk());

        // Validate the Supervisor in the database
        List<Supervisor> supervisorList = supervisorRepository.findAll();
        assertThat(supervisorList).hasSize(databaseSizeBeforeUpdate);
        Supervisor testSupervisor = supervisorList.get(supervisorList.size() - 1);
        assertThat(testSupervisor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupervisor.getStartTime()).isEqualTo(UPDATED_START_TIME);

        // Validate the Supervisor in Elasticsearch
        Supervisor supervisorEs = supervisorSearchRepository.findOne(testSupervisor.getId());
        assertThat(supervisorEs).isEqualToComparingFieldByField(testSupervisor);
    }

    @Test
    @Transactional
    public void updateNonExistingSupervisor() throws Exception {
        int databaseSizeBeforeUpdate = supervisorRepository.findAll().size();

        // Create the Supervisor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSupervisorMockMvc.perform(put("/api/supervisors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(supervisor)))
            .andExpect(status().isCreated());

        // Validate the Supervisor in the database
        List<Supervisor> supervisorList = supervisorRepository.findAll();
        assertThat(supervisorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSupervisor() throws Exception {
        // Initialize the database
        supervisorRepository.saveAndFlush(supervisor);
        supervisorSearchRepository.save(supervisor);
        int databaseSizeBeforeDelete = supervisorRepository.findAll().size();

        // Get the supervisor
        restSupervisorMockMvc.perform(delete("/api/supervisors/{id}", supervisor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean supervisorExistsInEs = supervisorSearchRepository.exists(supervisor.getId());
        assertThat(supervisorExistsInEs).isFalse();

        // Validate the database is empty
        List<Supervisor> supervisorList = supervisorRepository.findAll();
        assertThat(supervisorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSupervisor() throws Exception {
        // Initialize the database
        supervisorRepository.saveAndFlush(supervisor);
        supervisorSearchRepository.save(supervisor);

        // Search the supervisor
        restSupervisorMockMvc.perform(get("/api/_search/supervisors?query=id:" + supervisor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supervisor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supervisor.class);
        Supervisor supervisor1 = new Supervisor();
        supervisor1.setId(1L);
        Supervisor supervisor2 = new Supervisor();
        supervisor2.setId(supervisor1.getId());
        assertThat(supervisor1).isEqualTo(supervisor2);
        supervisor2.setId(2L);
        assertThat(supervisor1).isNotEqualTo(supervisor2);
        supervisor1.setId(null);
        assertThat(supervisor1).isNotEqualTo(supervisor2);
    }
}
