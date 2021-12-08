package com.pbpoints.console.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.console.domain.Category} entity.
 */
public class CategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String gameTimeType;

    @NotNull
    private Integer gameTime;

    @NotNull
    private String stopTimeType;

    private Integer stopTime;

    @NotNull
    private String stopSdTimeType;

    @NotNull
    private Integer stopSdTime;

    private Integer points;

    @NotNull
    private Integer difPoints;

    private EventDTO event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameTimeType() {
        return gameTimeType;
    }

    public void setGameTimeType(String gameTimeType) {
        this.gameTimeType = gameTimeType;
    }

    public Integer getGameTime() {
        return gameTime;
    }

    public void setGameTime(Integer gameTime) {
        this.gameTime = gameTime;
    }

    public String getStopTimeType() {
        return stopTimeType;
    }

    public void setStopTimeType(String stopTimeType) {
        this.stopTimeType = stopTimeType;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }

    public String getStopSdTimeType() {
        return stopSdTimeType;
    }

    public void setStopSdTimeType(String stopSdTimeType) {
        this.stopSdTimeType = stopSdTimeType;
    }

    public Integer getStopSdTime() {
        return stopSdTime;
    }

    public void setStopSdTime(Integer stopSdTime) {
        this.stopSdTime = stopSdTime;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getDifPoints() {
        return difPoints;
    }

    public void setDifPoints(Integer difPoints) {
        this.difPoints = difPoints;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryDTO{" +
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
            ", event=" + getEvent() +
            "}";
    }
}
