package com.pbpoints.console.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "game_time_type", nullable = false)
    private String gameTimeType;

    @NotNull
    @Column(name = "game_time", nullable = false)
    private Integer gameTime;

    @NotNull
    @Column(name = "stop_time_type", nullable = false)
    private String stopTimeType;

    @Column(name = "stop_time")
    private Integer stopTime;

    @NotNull
    @Column(name = "stop_sd_time_type", nullable = false)
    private String stopSdTimeType;

    @NotNull
    @Column(name = "stop_sd_time", nullable = false)
    private Integer stopSdTime;

    @Column(name = "points")
    private Integer points;

    @NotNull
    @Column(name = "dif_points", nullable = false)
    private Integer difPoints;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameTimeType() {
        return this.gameTimeType;
    }

    public Category gameTimeType(String gameTimeType) {
        this.gameTimeType = gameTimeType;
        return this;
    }

    public void setGameTimeType(String gameTimeType) {
        this.gameTimeType = gameTimeType;
    }

    public Integer getGameTime() {
        return this.gameTime;
    }

    public Category gameTime(Integer gameTime) {
        this.gameTime = gameTime;
        return this;
    }

    public void setGameTime(Integer gameTime) {
        this.gameTime = gameTime;
    }

    public String getStopTimeType() {
        return this.stopTimeType;
    }

    public Category stopTimeType(String stopTimeType) {
        this.stopTimeType = stopTimeType;
        return this;
    }

    public void setStopTimeType(String stopTimeType) {
        this.stopTimeType = stopTimeType;
    }

    public Integer getStopTime() {
        return this.stopTime;
    }

    public Category stopTime(Integer stopTime) {
        this.stopTime = stopTime;
        return this;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }

    public String getStopSdTimeType() {
        return this.stopSdTimeType;
    }

    public Category stopSdTimeType(String stopSdTimeType) {
        this.stopSdTimeType = stopSdTimeType;
        return this;
    }

    public void setStopSdTimeType(String stopSdTimeType) {
        this.stopSdTimeType = stopSdTimeType;
    }

    public Integer getStopSdTime() {
        return this.stopSdTime;
    }

    public Category stopSdTime(Integer stopSdTime) {
        this.stopSdTime = stopSdTime;
        return this;
    }

    public void setStopSdTime(Integer stopSdTime) {
        this.stopSdTime = stopSdTime;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Category points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getDifPoints() {
        return this.difPoints;
    }

    public Category difPoints(Integer difPoints) {
        this.difPoints = difPoints;
        return this;
    }

    public void setDifPoints(Integer difPoints) {
        this.difPoints = difPoints;
    }

    public Event getEvent() {
        return this.event;
    }

    public Category event(Event event) {
        this.setEvent(event);
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", gameTimeType='" + getGameTimeType() + "'" +
            ", gameTime=" + getGameTime() +
            ", stopTimeType='" + getStopTimeType() + "'" +
            ", stopTime=" + getStopTime() +
            ", stopSdTimeType='" + getStopSdTimeType() + "'" +
            ", stopSdTime=" + getStopSdTime() +
            ", points=" + getPoints() +
            ", difPoints=" + getDifPoints() +
            "}";
    }
}
