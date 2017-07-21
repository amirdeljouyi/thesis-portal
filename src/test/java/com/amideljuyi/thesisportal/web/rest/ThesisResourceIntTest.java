package com.amideljuyi.thesisportal.web.rest;

import com.amideljuyi.thesisportal.ThesisPortalApp;

import com.amideljuyi.thesisportal.domain.Thesis;
import com.amideljuyi.thesisportal.repository.ThesisRepository;
import com.amideljuyi.thesisportal.repository.search.ThesisSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ThesisResource REST controller.
 *
 * @see ThesisResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisPortalApp.class)
public class ThesisResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MAJOR_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MAJOR_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DAY_OF_DEFENSE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DAY_OF_DEFENSE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCATION_OF_DEFENSE = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_OF_DEFENSE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private ThesisSearchRepository thesisSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restThesisMockMvc;

    private Thesis thesis;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ThesisResource thesisResource = new ThesisResource(thesisRepository, thesisSearchRepository);
        this.restThesisMockMvc = MockMvcBuilders.standaloneSetup(thesisResource)
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
    public static Thesis createEntity(EntityManager em) {
        Thesis thesis = new Thesis()
            .title(DEFAULT_TITLE)
            .majorTitle(DEFAULT_MAJOR_TITLE)
            .summary(DEFAULT_SUMMARY)
            .dayOfDefense(DEFAULT_DAY_OF_DEFENSE)
            .locationOfDefense(DEFAULT_LOCATION_OF_DEFENSE)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        return thesis;
    }

    @Before
    public void initTest() {
        thesisSearchRepository.deleteAll();
        thesis = createEntity(em);
    }

    @Test
    @Transactional
    public void createThesis() throws Exception {
        int databaseSizeBeforeCreate = thesisRepository.findAll().size();

        // Create the Thesis
        restThesisMockMvc.perform(post("/api/theses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thesis)))
            .andExpect(status().isCreated());

        // Validate the Thesis in the database
        List<Thesis> thesisList = thesisRepository.findAll();
        assertThat(thesisList).hasSize(databaseSizeBeforeCreate + 1);
        Thesis testThesis = thesisList.get(thesisList.size() - 1);
        assertThat(testThesis.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testThesis.getMajorTitle()).isEqualTo(DEFAULT_MAJOR_TITLE);
        assertThat(testThesis.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testThesis.getDayOfDefense()).isEqualTo(DEFAULT_DAY_OF_DEFENSE);
        assertThat(testThesis.getLocationOfDefense()).isEqualTo(DEFAULT_LOCATION_OF_DEFENSE);
        assertThat(testThesis.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testThesis.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);

        // Validate the Thesis in Elasticsearch
        Thesis thesisEs = thesisSearchRepository.findOne(testThesis.getId());
        assertThat(thesisEs).isEqualToComparingFieldByField(testThesis);
    }

    @Test
    @Transactional
    public void createThesisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = thesisRepository.findAll().size();

        // Create the Thesis with an existing ID
        thesis.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThesisMockMvc.perform(post("/api/theses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thesis)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Thesis> thesisList = thesisRepository.findAll();
        assertThat(thesisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = thesisRepository.findAll().size();
        // set the field null
        thesis.setTitle(null);

        // Create the Thesis, which fails.

        restThesisMockMvc.perform(post("/api/theses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thesis)))
            .andExpect(status().isBadRequest());

        List<Thesis> thesisList = thesisRepository.findAll();
        assertThat(thesisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTheses() throws Exception {
        // Initialize the database
        thesisRepository.saveAndFlush(thesis);

        // Get all the thesisList
        restThesisMockMvc.perform(get("/api/theses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thesis.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].majorTitle").value(hasItem(DEFAULT_MAJOR_TITLE.toString())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].dayOfDefense").value(hasItem(DEFAULT_DAY_OF_DEFENSE.toString())))
            .andExpect(jsonPath("$.[*].locationOfDefense").value(hasItem(DEFAULT_LOCATION_OF_DEFENSE.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }

    @Test
    @Transactional
    public void getThesis() throws Exception {
        // Initialize the database
        thesisRepository.saveAndFlush(thesis);

        // Get the thesis
        restThesisMockMvc.perform(get("/api/theses/{id}", thesis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(thesis.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.majorTitle").value(DEFAULT_MAJOR_TITLE.toString()))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.dayOfDefense").value(DEFAULT_DAY_OF_DEFENSE.toString()))
            .andExpect(jsonPath("$.locationOfDefense").value(DEFAULT_LOCATION_OF_DEFENSE.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingThesis() throws Exception {
        // Get the thesis
        restThesisMockMvc.perform(get("/api/theses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThesis() throws Exception {
        // Initialize the database
        thesisRepository.saveAndFlush(thesis);
        thesisSearchRepository.save(thesis);
        int databaseSizeBeforeUpdate = thesisRepository.findAll().size();

        // Update the thesis
        Thesis updatedThesis = thesisRepository.findOne(thesis.getId());
        updatedThesis
            .title(UPDATED_TITLE)
            .majorTitle(UPDATED_MAJOR_TITLE)
            .summary(UPDATED_SUMMARY)
            .dayOfDefense(UPDATED_DAY_OF_DEFENSE)
            .locationOfDefense(UPDATED_LOCATION_OF_DEFENSE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restThesisMockMvc.perform(put("/api/theses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedThesis)))
            .andExpect(status().isOk());

        // Validate the Thesis in the database
        List<Thesis> thesisList = thesisRepository.findAll();
        assertThat(thesisList).hasSize(databaseSizeBeforeUpdate);
        Thesis testThesis = thesisList.get(thesisList.size() - 1);
        assertThat(testThesis.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testThesis.getMajorTitle()).isEqualTo(UPDATED_MAJOR_TITLE);
        assertThat(testThesis.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testThesis.getDayOfDefense()).isEqualTo(UPDATED_DAY_OF_DEFENSE);
        assertThat(testThesis.getLocationOfDefense()).isEqualTo(UPDATED_LOCATION_OF_DEFENSE);
        assertThat(testThesis.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testThesis.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);

        // Validate the Thesis in Elasticsearch
        Thesis thesisEs = thesisSearchRepository.findOne(testThesis.getId());
        assertThat(thesisEs).isEqualToComparingFieldByField(testThesis);
    }

    @Test
    @Transactional
    public void updateNonExistingThesis() throws Exception {
        int databaseSizeBeforeUpdate = thesisRepository.findAll().size();

        // Create the Thesis

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restThesisMockMvc.perform(put("/api/theses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(thesis)))
            .andExpect(status().isCreated());

        // Validate the Thesis in the database
        List<Thesis> thesisList = thesisRepository.findAll();
        assertThat(thesisList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteThesis() throws Exception {
        // Initialize the database
        thesisRepository.saveAndFlush(thesis);
        thesisSearchRepository.save(thesis);
        int databaseSizeBeforeDelete = thesisRepository.findAll().size();

        // Get the thesis
        restThesisMockMvc.perform(delete("/api/theses/{id}", thesis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean thesisExistsInEs = thesisSearchRepository.exists(thesis.getId());
        assertThat(thesisExistsInEs).isFalse();

        // Validate the database is empty
        List<Thesis> thesisList = thesisRepository.findAll();
        assertThat(thesisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchThesis() throws Exception {
        // Initialize the database
        thesisRepository.saveAndFlush(thesis);
        thesisSearchRepository.save(thesis);

        // Search the thesis
        restThesisMockMvc.perform(get("/api/_search/theses?query=id:" + thesis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thesis.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].majorTitle").value(hasItem(DEFAULT_MAJOR_TITLE.toString())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].dayOfDefense").value(hasItem(DEFAULT_DAY_OF_DEFENSE.toString())))
            .andExpect(jsonPath("$.[*].locationOfDefense").value(hasItem(DEFAULT_LOCATION_OF_DEFENSE.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thesis.class);
        Thesis thesis1 = new Thesis();
        thesis1.setId(1L);
        Thesis thesis2 = new Thesis();
        thesis2.setId(thesis1.getId());
        assertThat(thesis1).isEqualTo(thesis2);
        thesis2.setId(2L);
        assertThat(thesis1).isNotEqualTo(thesis2);
        thesis1.setId(null);
        assertThat(thesis1).isNotEqualTo(thesis2);
    }
}
