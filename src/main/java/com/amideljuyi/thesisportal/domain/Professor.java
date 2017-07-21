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

/**
 * A Professor.
 */
@Entity
@Table(name = "professor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "professor")
public class Professor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "job")
    private String job;

    @Column(name = "free_capacity_of_year")
    private Integer freeCapacityOfYear;

    @Column(name = "free_capacity_of_total")
    private Integer freeCapacityOfTotal;

    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Referee> referees = new HashSet<>();

    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Adviser> advisers = new HashSet<>();

    @OneToMany(mappedBy = "professor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Supervisor> supervisors = new HashSet<>();

    @ManyToOne
    private Level level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Professor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public Professor job(String job) {
        this.job = job;
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getFreeCapacityOfYear() {
        return freeCapacityOfYear;
    }

    public Professor freeCapacityOfYear(Integer freeCapacityOfYear) {
        this.freeCapacityOfYear = freeCapacityOfYear;
        return this;
    }

    public void setFreeCapacityOfYear(Integer freeCapacityOfYear) {
        this.freeCapacityOfYear = freeCapacityOfYear;
    }

    public Integer getFreeCapacityOfTotal() {
        return freeCapacityOfTotal;
    }

    public Professor freeCapacityOfTotal(Integer freeCapacityOfTotal) {
        this.freeCapacityOfTotal = freeCapacityOfTotal;
        return this;
    }

    public void setFreeCapacityOfTotal(Integer freeCapacityOfTotal) {
        this.freeCapacityOfTotal = freeCapacityOfTotal;
    }

    public Set<Referee> getReferees() {
        return referees;
    }

    public Professor referees(Set<Referee> referees) {
        this.referees = referees;
        return this;
    }

    public Professor addReferee(Referee referee) {
        this.referees.add(referee);
        referee.setProfessor(this);
        return this;
    }

    public Professor removeReferee(Referee referee) {
        this.referees.remove(referee);
        referee.setProfessor(null);
        return this;
    }

    public void setReferees(Set<Referee> referees) {
        this.referees = referees;
    }

    public Set<Adviser> getAdvisers() {
        return advisers;
    }

    public Professor advisers(Set<Adviser> advisers) {
        this.advisers = advisers;
        return this;
    }

    public Professor addAdviser(Adviser adviser) {
        this.advisers.add(adviser);
        adviser.setProfessor(this);
        return this;
    }

    public Professor removeAdviser(Adviser adviser) {
        this.advisers.remove(adviser);
        adviser.setProfessor(null);
        return this;
    }

    public void setAdvisers(Set<Adviser> advisers) {
        this.advisers = advisers;
    }

    public Set<Supervisor> getSupervisors() {
        return supervisors;
    }

    public Professor supervisors(Set<Supervisor> supervisors) {
        this.supervisors = supervisors;
        return this;
    }

    public Professor addSupervisor(Supervisor supervisor) {
        this.supervisors.add(supervisor);
        supervisor.setProfessor(this);
        return this;
    }

    public Professor removeSupervisor(Supervisor supervisor) {
        this.supervisors.remove(supervisor);
        supervisor.setProfessor(null);
        return this;
    }

    public void setSupervisors(Set<Supervisor> supervisors) {
        this.supervisors = supervisors;
    }

    public Level getLevel() {
        return level;
    }

    public Professor level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Professor professor = (Professor) o;
        if (professor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), professor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Professor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", job='" + getJob() + "'" +
            ", freeCapacityOfYear='" + getFreeCapacityOfYear() + "'" +
            ", freeCapacityOfTotal='" + getFreeCapacityOfTotal() + "'" +
            "}";
    }
}
