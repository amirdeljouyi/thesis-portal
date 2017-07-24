package com.amideljuyi.thesisportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Thesis.
 */
@Entity
@Table(name = "thesis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "thesis")
public class Thesis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "major_title")
    private String majorTitle;

    @Column(name = "summary")
    private String summary;

    @Column(name = "day_of_defense")
    private Instant dayOfDefense;

    @Column(name = "location_of_defense")
    private String locationOfDefense;

    @Lob
    @Column(name = "jhi_file")
    private byte[] file;

    @Column(name = "jhi_file_content_type")
    private String fileContentType;

    @Min(value = 0)
    @Max(value = 2)
    @Column(name = "num_of_referee")
    private Integer numOfReferee;

    @OneToOne
    @JoinColumn(unique = true)
    private Student student;

    @OneToMany(mappedBy = "thesis")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Referee> referees = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Thesis title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMajorTitle() {
        return majorTitle;
    }

    public Thesis majorTitle(String majorTitle) {
        this.majorTitle = majorTitle;
        return this;
    }

    public void setMajorTitle(String majorTitle) {
        this.majorTitle = majorTitle;
    }

    public String getSummary() {
        return summary;
    }

    public Thesis summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Instant getDayOfDefense() {
        return dayOfDefense;
    }

    public Thesis dayOfDefense(Instant dayOfDefense) {
        this.dayOfDefense = dayOfDefense;
        return this;
    }

    public void setDayOfDefense(Instant dayOfDefense) {
        this.dayOfDefense = dayOfDefense;
    }

    public String getLocationOfDefense() {
        return locationOfDefense;
    }

    public Thesis locationOfDefense(String locationOfDefense) {
        this.locationOfDefense = locationOfDefense;
        return this;
    }

    public void setLocationOfDefense(String locationOfDefense) {
        this.locationOfDefense = locationOfDefense;
    }

    public byte[] getFile() {
        return file;
    }

    public Thesis file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public Thesis fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Integer getNumOfReferee() {
        return numOfReferee;
    }

    public Thesis numOfReferee(Integer numOfReferee) {
        this.numOfReferee = numOfReferee;
        return this;
    }

    public void setNumOfReferee(Integer numOfReferee) {
        this.numOfReferee = numOfReferee;
    }

    public Student getStudent() {
        return student;
    }

    public Thesis student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Set<Referee> getReferees() {
        return referees;
    }

    public Thesis referees(Set<Referee> referees) {
        this.referees = referees;
        return this;
    }

    public Thesis addReferee(Referee referee) {
        this.referees.add(referee);
        referee.setThesis(this);
        return this;
    }

    public Thesis removeReferee(Referee referee) {
        this.referees.remove(referee);
        referee.setThesis(null);
        return this;
    }

    public void setReferees(Set<Referee> referees) {
        this.referees = referees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Thesis thesis = (Thesis) o;
        if (thesis.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), thesis.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Thesis{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", majorTitle='" + getMajorTitle() + "'" +
            ", summary='" + getSummary() + "'" +
            ", dayOfDefense='" + getDayOfDefense() + "'" +
            ", locationOfDefense='" + getLocationOfDefense() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + fileContentType + "'" +
            ", numOfReferee='" + getNumOfReferee() + "'" +
            "}";
    }
}
