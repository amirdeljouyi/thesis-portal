package com.amideljuyi.thesisportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.amideljuyi.thesisportal.domain.enumeration.Status;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "year_of_enter", nullable = false)
    private Integer yearOfEnter;

    @Max(value = 2)
    @Column(name = "num_of_supervisor")
    private Integer numOfSupervisor;

    @Max(value = 2)
    @Column(name = "num_of_adviser")
    private Integer numOfAdviser;

    @NotNull
    @Min(value = 1000000000L)
    @Max(value = 9999999999L)
    @Column(name = "student_number", nullable = false)
    private Long studentNumber;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Adviser> advisers = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Supervisor> supervisers = new HashSet<>();

    @OneToOne(mappedBy = "student")
    @JsonIgnore
    private Thesis thesis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Student name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public Student status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getYearOfEnter() {
        return yearOfEnter;
    }

    public Student yearOfEnter(Integer yearOfEnter) {
        this.yearOfEnter = yearOfEnter;
        return this;
    }

    public void setYearOfEnter(Integer yearOfEnter) {
        this.yearOfEnter = yearOfEnter;
    }

    public Integer getNumOfSupervisor() {
        return numOfSupervisor;
    }

    public Student numOfSupervisor(Integer numOfSupervisor) {
        this.numOfSupervisor = numOfSupervisor;
        return this;
    }

    public void setNumOfSupervisor(Integer numOfSupervisor) {
        this.numOfSupervisor = numOfSupervisor;
    }

    public Integer getNumOfAdviser() {
        return numOfAdviser;
    }

    public Student numOfAdviser(Integer numOfAdviser) {
        this.numOfAdviser = numOfAdviser;
        return this;
    }

    public void setNumOfAdviser(Integer numOfAdviser) {
        this.numOfAdviser = numOfAdviser;
    }

    public Long getStudentNumber() {
        return studentNumber;
    }

    public Student studentNumber(Long studentNumber) {
        this.studentNumber = studentNumber;
        return this;
    }

    public void setStudentNumber(Long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Set<Adviser> getAdvisers() {
        return advisers;
    }

    public Student advisers(Set<Adviser> advisers) {
        this.advisers = advisers;
        return this;
    }

    public Student addAdviser(Adviser adviser) {
        this.advisers.add(adviser);
        adviser.setStudent(this);
        return this;
    }

    public Student removeAdviser(Adviser adviser) {
        this.advisers.remove(adviser);
        adviser.setStudent(null);
        return this;
    }

    public void setAdvisers(Set<Adviser> advisers) {
        this.advisers = advisers;
    }

    public Set<Supervisor> getSupervisers() {
        return supervisers;
    }

    public Student supervisers(Set<Supervisor> supervisors) {
        this.supervisers = supervisors;
        return this;
    }

    public Student addSuperviser(Supervisor supervisor) {
        this.supervisers.add(supervisor);
        supervisor.setStudent(this);
        return this;
    }

    public Student removeSuperviser(Supervisor supervisor) {
        this.supervisers.remove(supervisor);
        supervisor.setStudent(null);
        return this;
    }

    public void setSupervisers(Set<Supervisor> supervisors) {
        this.supervisers = supervisors;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public Student thesis(Thesis thesis) {
        this.thesis = thesis;
        return this;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        if (student.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", yearOfEnter='" + getYearOfEnter() + "'" +
            ", numOfSupervisor='" + getNumOfSupervisor() + "'" +
            ", numOfAdviser='" + getNumOfAdviser() + "'" +
            ", studentNumber='" + getStudentNumber() + "'" +
            "}";
    }
}
