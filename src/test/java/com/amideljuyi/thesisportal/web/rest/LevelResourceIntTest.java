package com.amideljuyi.thesisportal.web.rest;

import com.amideljuyi.thesisportal.ThesisPortalApp;

import com.amideljuyi.thesisportal.domain.Level;
import com.amideljuyi.thesisportal.repository.LevelRepository;
import com.amideljuyi.thesisportal.repository.search.LevelSearchRepository;
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

import com.amideljuyi.thesisportal.domain.enumeration.ProfessorLevel;
/**
 * Test class for the LevelResource REST controller.
 *
 * @see LevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisPortalApp.class)
public class LevelResourceIntTest {

    private static final ProfessorLevel DEFAULT_LEVEL = ProfessorLevel.PROFESSOR;
    private static final ProfessorLevel UPDATED_LEVEL = ProfessorLevel.ASSISTANTPROFESSOR;

    private static final Integer DEFAULT_CAPACITY_OF_YEAR = 0;
    private static final Integer UPDATED_CAPACITY_OF_YEAR = 1;

    private static final Integer DEFAULT_CAPACITY_OF_TOTAL = 0;
    private static final Integer UPDATED_CAPACITY_OF_TOTAL = 1;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private LevelSearchRepository levelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLevelMockMvc;

    private Level level;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LevelResource levelResource = new LevelResource(levelRepository, levelSearchRepository);
        this.restLevelMockMvc = MockMvcBuilders.standaloneSetup(levelResource)
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
    public static Level createEntity(EntityManager em) {
        Level level = new Level()
            .level(DEFAULT_LEVEL)
            .capacityOfYear(DEFAULT_CAPACITY_OF_YEAR)
            .capacityOfTotal(DEFAULT_CAPACITY_OF_TOTAL);
        return level;
    }

    @Before
    public void initTest() {
        levelSearchRepository.deleteAll();
        level = createEntity(em);
    }

    @Test
    @Transactional
    public void createLevel() throws Exception {
        int databaseSizeBeforeCreate = levelRepository.findAll().size();

        // Create the Level
        restLevelMockMvc.perform(post("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(level)))
            .andExpect(status().isCreated());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate + 1);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testLevel.getCapacityOfYear()).isEqualTo(DEFAULT_CAPACITY_OF_YEAR);
        assertThat(testLevel.getCapacityOfTotal()).isEqualTo(DEFAULT_CAPACITY_OF_TOTAL);

        // Validate the Level in Elasticsearch
        Level levelEs = levelSearchRepository.findOne(testLevel.getId());
        assertThat(levelEs).isEqualToComparingFieldByField(testLevel);
    }

    @Test
    @Transactional
    public void createLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = levelRepository.findAll().size();

        // Create the Level with an existing ID
        level.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLevelMockMvc.perform(post("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(level)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLevels() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get all the levelList
        restLevelMockMvc.perform(get("/api/levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(level.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].capacityOfYear").value(hasItem(DEFAULT_CAPACITY_OF_YEAR)))
            .andExpect(jsonPath("$.[*].capacityOfTotal").value(hasItem(DEFAULT_CAPACITY_OF_TOTAL)));
    }

    @Test
    @Transactional
    public void getLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get the level
        restLevelMockMvc.perform(get("/api/levels/{id}", level.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(level.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.capacityOfYear").value(DEFAULT_CAPACITY_OF_YEAR))
            .andExpect(jsonPath("$.capacityOfTotal").value(DEFAULT_CAPACITY_OF_TOTAL));
    }

    @Test
    @Transactional
    public void getNonExistingLevel() throws Exception {
        // Get the level
        restLevelMockMvc.perform(get("/api/levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        levelSearchRepository.save(level);
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Update the level
        Level updatedLevel = levelRepository.findOne(level.getId());
        updatedLevel
            .level(UPDATED_LEVEL)
            .capacityOfYear(UPDATED_CAPACITY_OF_YEAR)
            .capacityOfTotal(UPDATED_CAPACITY_OF_TOTAL);

        restLevelMockMvc.perform(put("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLevel)))
            .andExpect(status().isOk());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testLevel.getCapacityOfYear()).isEqualTo(UPDATED_CAPACITY_OF_YEAR);
        assertThat(testLevel.getCapacityOfTotal()).isEqualTo(UPDATED_CAPACITY_OF_TOTAL);

        // Validate the Level in Elasticsearch
        Level levelEs = levelSearchRepository.findOne(testLevel.getId());
        assertThat(levelEs).isEqualToComparingFieldByField(testLevel);
    }

    @Test
    @Transactional
    public void updateNonExistingLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Create the Level

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLevelMockMvc.perform(put("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(level)))
            .andExpect(status().isCreated());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        levelSearchRepository.save(level);
        int databaseSizeBeforeDelete = levelRepository.findAll().size();

        // Get the level
        restLevelMockMvc.perform(delete("/api/levels/{id}", level.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean levelExistsInEs = levelSearchRepository.exists(level.getId());
        assertThat(levelExistsInEs).isFalse();

        // Validate the database is empty
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        levelSearchRepository.save(level);

        // Search the level
        restLevelMockMvc.perform(get("/api/_search/levels?query=id:" + level.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(level.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].capacityOfYear").value(hasItem(DEFAULT_CAPACITY_OF_YEAR)))
            .andExpect(jsonPath("$.[*].capacityOfTotal").value(hasItem(DEFAULT_CAPACITY_OF_TOTAL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Level.class);
        Level level1 = new Level();
        level1.setId(1L);
        Level level2 = new Level();
        level2.setId(level1.getId());
        assertThat(level1).isEqualTo(level2);
        level2.setId(2L);
        assertThat(level1).isNotEqualTo(level2);
        level1.setId(null);
        assertThat(level1).isNotEqualTo(level2);
    }
}
