package com.pbpoints.console.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pbpoints.console.domain.Category} entity. This class is used
 * in {@link com.pbpoints.console.web.rest.CategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter gameTimeType;

    private IntegerFilter gameTime;

    private StringFilter stopTimeType;

    private IntegerFilter stopTime;

    private StringFilter stopSdTimeType;

    private IntegerFilter stopSdTime;

    private IntegerFilter points;

    private IntegerFilter difPoints;

    private LongFilter eventId;

    public CategoryCriteria() {}

    public CategoryCriteria(CategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.gameTimeType = other.gameTimeType == null ? null : other.gameTimeType.copy();
        this.gameTime = other.gameTime == null ? null : other.gameTime.copy();
        this.stopTimeType = other.stopTimeType == null ? null : other.stopTimeType.copy();
        this.stopTime = other.stopTime == null ? null : other.stopTime.copy();
        this.stopSdTimeType = other.stopSdTimeType == null ? null : other.stopSdTimeType.copy();
        this.stopSdTime = other.stopSdTime == null ? null : other.stopSdTime.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.difPoints = other.difPoints == null ? null : other.difPoints.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
    }

    @Override
    public CategoryCriteria copy() {
        return new CategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getGameTimeType() {
        return gameTimeType;
    }

    public StringFilter gameTimeType() {
        if (gameTimeType == null) {
            gameTimeType = new StringFilter();
        }
        return gameTimeType;
    }

    public void setGameTimeType(StringFilter gameTimeType) {
        this.gameTimeType = gameTimeType;
    }

    public IntegerFilter getGameTime() {
        return gameTime;
    }

    public IntegerFilter gameTime() {
        if (gameTime == null) {
            gameTime = new IntegerFilter();
        }
        return gameTime;
    }

    public void setGameTime(IntegerFilter gameTime) {
        this.gameTime = gameTime;
    }

    public StringFilter getStopTimeType() {
        return stopTimeType;
    }

    public StringFilter stopTimeType() {
        if (stopTimeType == null) {
            stopTimeType = new StringFilter();
        }
        return stopTimeType;
    }

    public void setStopTimeType(StringFilter stopTimeType) {
        this.stopTimeType = stopTimeType;
    }

    public IntegerFilter getStopTime() {
        return stopTime;
    }

    public IntegerFilter stopTime() {
        if (stopTime == null) {
            stopTime = new IntegerFilter();
        }
        return stopTime;
    }

    public void setStopTime(IntegerFilter stopTime) {
        this.stopTime = stopTime;
    }

    public StringFilter getStopSdTimeType() {
        return stopSdTimeType;
    }

    public StringFilter stopSdTimeType() {
        if (stopSdTimeType == null) {
            stopSdTimeType = new StringFilter();
        }
        return stopSdTimeType;
    }

    public void setStopSdTimeType(StringFilter stopSdTimeType) {
        this.stopSdTimeType = stopSdTimeType;
    }

    public IntegerFilter getStopSdTime() {
        return stopSdTime;
    }

    public IntegerFilter stopSdTime() {
        if (stopSdTime == null) {
            stopSdTime = new IntegerFilter();
        }
        return stopSdTime;
    }

    public void setStopSdTime(IntegerFilter stopSdTime) {
        this.stopSdTime = stopSdTime;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public IntegerFilter points() {
        if (points == null) {
            points = new IntegerFilter();
        }
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public IntegerFilter getDifPoints() {
        return difPoints;
    }

    public IntegerFilter difPoints() {
        if (difPoints == null) {
            difPoints = new IntegerFilter();
        }
        return difPoints;
    }

    public void setDifPoints(IntegerFilter difPoints) {
        this.difPoints = difPoints;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryCriteria that = (CategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(gameTimeType, that.gameTimeType) &&
            Objects.equals(gameTime, that.gameTime) &&
            Objects.equals(stopTimeType, that.stopTimeType) &&
            Objects.equals(stopTime, that.stopTime) &&
            Objects.equals(stopSdTimeType, that.stopSdTimeType) &&
            Objects.equals(stopSdTime, that.stopSdTime) &&
            Objects.equals(points, that.points) &&
            Objects.equals(difPoints, that.difPoints) &&
            Objects.equals(eventId, that.eventId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            gameTimeType,
            gameTime,
            stopTimeType,
            stopTime,
            stopSdTimeType,
            stopSdTime,
            points,
            difPoints,
            eventId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (gameTimeType != null ? "gameTimeType=" + gameTimeType + ", " : "") +
            (gameTime != null ? "gameTime=" + gameTime + ", " : "") +
            (stopTimeType != null ? "stopTimeType=" + stopTimeType + ", " : "") +
            (stopTime != null ? "stopTime=" + stopTime + ", " : "") +
            (stopSdTimeType != null ? "stopSdTimeType=" + stopSdTimeType + ", " : "") +
            (stopSdTime != null ? "stopSdTime=" + stopSdTime + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (difPoints != null ? "difPoints=" + difPoints + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            "}";
    }
}
