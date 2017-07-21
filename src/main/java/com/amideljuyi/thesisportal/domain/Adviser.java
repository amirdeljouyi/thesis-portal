package com.amideljuyi.thesisportal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Adviser.
 */
@Entity
@Table(name = "adviser")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "adviser")
public class Adviser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @ManyToOne
    private Professor professor;

    @ManyToOne
    private Student student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Adviser name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Adviser startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Adviser professor(Professor professor) {
        this.professor = professor;
        return this;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Student getStudent() {
        return student;
    }

    public Adviser student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Adviser adviser = (Adviser) o;
        if (adviser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adviser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Adviser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            "}";
    }
}
