package com.amideljuyi.thesisportal.web.rest;

import com.amideljuyi.thesisportal.ThesisPortalApp;

import com.amideljuyi.thesisportal.domain.Student;
import com.amideljuyi.thesisportal.repository.StudentRepository;
import com.amideljuyi.thesisportal.repository.search.StudentSearchRepository;
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

import com.amideljuyi.thesisportal.domain.enumeration.Status;
/**
 * Test class for the StudentResource REST controller.
 *
 * @see StudentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisPortalApp.class)
public class StudentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.INPRORGESS;
    private static final Status UPDATED_STATUS = Status.DISSMISSED;

    private static final Integer DEFAULT_YEAR_OF_ENTER = 1;
    private static final Integer UPDATED_YEAR_OF_ENTER = 2;

    private static final Integer DEFAULT_NUM_OF_SUPERVISOR = 2;
    private static final Integer UPDATED_NUM_OF_SUPERVISOR = 1;

    private static final Integer DEFAULT_NUM_OF_ADVISER = 2;
    private static final Integer UPDATED_NUM_OF_ADVISER = 1;

    private static final Long DEFAULT_STUDENT_NUMBER = 1000000000L;
    private static final Long UPDATED_STUDENT_NUMBER = 1000000001L;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentSearchRepository studentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentMockMvc;

    private Student student;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentResource studentResource = new StudentResource(studentRepository, studentSearchRepository);
        this.restStudentMockMvc = MockMvcBuilders.standaloneSetup(studentResource)
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
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .yearOfEnter(DEFAULT_YEAR_OF_ENTER)
            .numOfSupervisor(DEFAULT_NUM_OF_SUPERVISOR)
            .numOfAdviser(DEFAULT_NUM_OF_ADVISER)
            .studentNumber(DEFAULT_STUDENT_NUMBER);
        return student;
    }

    @Before
    public void initTest() {
        studentSearchRepository.deleteAll();
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudent.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStudent.getYearOfEnter()).isEqualTo(DEFAULT_YEAR_OF_ENTER);
        assertThat(testStudent.getNumOfSupervisor()).isEqualTo(DEFAULT_NUM_OF_SUPERVISOR);
        assertThat(testStudent.getNumOfAdviser()).isEqualTo(DEFAULT_NUM_OF_ADVISER);
        assertThat(testStudent.getStudentNumber()).isEqualTo(DEFAULT_STUDENT_NUMBER);

        // Validate the Student in Elasticsearch
        Student studentEs = studentSearchRepository.findOne(testStudent.getId());
        assertThat(studentEs).isEqualToComparingFieldByField(testStudent);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setName(null);

        // Create the Student, which fails.

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setStatus(null);

        // Create the Student, which fails.

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearOfEnterIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setYearOfEnter(null);

        // Create the Student, which fails.

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStudentNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setStudentNumber(null);

        // Create the Student, which fails.

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].yearOfEnter").value(hasItem(DEFAULT_YEAR_OF_ENTER)))
            .andExpect(jsonPath("$.[*].numOfSupervisor").value(hasItem(DEFAULT_NUM_OF_SUPERVISOR)))
            .andExpect(jsonPath("$.[*].numOfAdviser").value(hasItem(DEFAULT_NUM_OF_ADVISER)))
            .andExpect(jsonPath("$.[*].studentNumber").value(hasItem(DEFAULT_STUDENT_NUMBER.intValue())));
    }

    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.yearOfEnter").value(DEFAULT_YEAR_OF_ENTER))
            .andExpect(jsonPath("$.numOfSupervisor").value(DEFAULT_NUM_OF_SUPERVISOR))
            .andExpect(jsonPath("$.numOfAdviser").value(DEFAULT_NUM_OF_ADVISER))
            .andExpect(jsonPath("$.studentNumber").value(DEFAULT_STUDENT_NUMBER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        studentSearchRepository.save(student);
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findOne(student.getId());
        updatedStudent
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .yearOfEnter(UPDATED_YEAR_OF_ENTER)
            .numOfSupervisor(UPDATED_NUM_OF_SUPERVISOR)
            .numOfAdviser(UPDATED_NUM_OF_ADVISER)
            .studentNumber(UPDATED_STUDENT_NUMBER);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudent)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStudent.getYearOfEnter()).isEqualTo(UPDATED_YEAR_OF_ENTER);
        assertThat(testStudent.getNumOfSupervisor()).isEqualTo(UPDATED_NUM_OF_SUPERVISOR);
        assertThat(testStudent.getNumOfAdviser()).isEqualTo(UPDATED_NUM_OF_ADVISER);
        assertThat(testStudent.getStudentNumber()).isEqualTo(UPDATED_STUDENT_NUMBER);

        // Validate the Student in Elasticsearch
        Student studentEs = studentSearchRepository.findOne(testStudent.getId());
        assertThat(studentEs).isEqualToComparingFieldByField(testStudent);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Create the Student

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        studentSearchRepository.save(student);
        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Get the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean studentExistsInEs = studentSearchRepository.exists(student.getId());
        assertThat(studentExistsInEs).isFalse();

        // Validate the database is empty
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        studentSearchRepository.save(student);

        // Search the student
        restStudentMockMvc.perform(get("/api/_search/students?query=id:" + student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].yearOfEnter").value(hasItem(DEFAULT_YEAR_OF_ENTER)))
            .andExpect(jsonPath("$.[*].numOfSupervisor").value(hasItem(DEFAULT_NUM_OF_SUPERVISOR)))
            .andExpect(jsonPath("$.[*].numOfAdviser").value(hasItem(DEFAULT_NUM_OF_ADVISER)))
            .andExpect(jsonPath("$.[*].studentNumber").value(hasItem(DEFAULT_STUDENT_NUMBER.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);
        student2.setId(2L);
        assertThat(student1).isNotEqualTo(student2);
        student1.setId(null);
        assertThat(student1).isNotEqualTo(student2);
    }
}
