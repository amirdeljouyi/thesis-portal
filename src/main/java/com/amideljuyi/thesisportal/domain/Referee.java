package com.amideljuyi.thesisportal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Referee.
 */
@Entity
@Table(name = "referee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "referee")
public class Referee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Thesis thesis;

    @ManyToOne
    private Professor professor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Referee name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public Referee thesis(Thesis thesis) {
        this.thesis = thesis;
        return this;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Referee professor(Professor professor) {
        this.professor = professor;
        return this;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Referee referee = (Referee) o;
        if (referee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), referee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Referee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
