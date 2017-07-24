package com.amideljuyi.thesisportal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.amideljuyi.thesisportal.domain.enumeration.ProfessorLevel;

/**
 * A Level.
 */
@Entity
@Table(name = "level")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "level")
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_level")
    private ProfessorLevel level;

    @Min(value = 0)
    @Max(value = 20)
    @Column(name = "capacity_of_year")
    private Integer capacityOfYear;

    @Min(value = 0)
    @Max(value = 50)
    @Column(name = "capacity_of_total")
    private Integer capacityOfTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfessorLevel getLevel() {
        return level;
    }

    public Level level(ProfessorLevel level) {
        this.level = level;
        return this;
    }

    public void setLevel(ProfessorLevel level) {
        this.level = level;
    }

    public Integer getCapacityOfYear() {
        return capacityOfYear;
    }

    public Level capacityOfYear(Integer capacityOfYear) {
        this.capacityOfYear = capacityOfYear;
        return this;
    }

    public void setCapacityOfYear(Integer capacityOfYear) {
        this.capacityOfYear = capacityOfYear;
    }

    public Integer getCapacityOfTotal() {
        return capacityOfTotal;
    }

    public Level capacityOfTotal(Integer capacityOfTotal) {
        this.capacityOfTotal = capacityOfTotal;
        return this;
    }

    public void setCapacityOfTotal(Integer capacityOfTotal) {
        this.capacityOfTotal = capacityOfTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Level level = (Level) o;
        if (level.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), level.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Level{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", capacityOfYear='" + getCapacityOfYear() + "'" +
            ", capacityOfTotal='" + getCapacityOfTotal() + "'" +
            "}";
    }
}
