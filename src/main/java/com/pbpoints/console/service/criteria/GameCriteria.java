package com.pbpoints.console.service.criteria;

import com.pbpoints.console.domain.enumeration.Status;
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
 * Criteria class for the {@link com.pbpoints.console.domain.Game} entity. This class is used
 * in {@link com.pbpoints.console.web.rest.GameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /games?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GameCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter gameNum;

    private StringFilter clasif;

    private IntegerFilter splitdeckNum;

    private IntegerFilter pointsA;

    private IntegerFilter pointsB;

    private IntegerFilter overA;

    private IntegerFilter overB;

    private IntegerFilter pvpA;

    private IntegerFilter pvpB;

    private IntegerFilter timeLeft;

    private StatusFilter status;

    private LongFilter teamAId;

    private LongFilter teamBId;

    private LongFilter categoryId;

    public GameCriteria() {}

    public GameCriteria(GameCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.gameNum = other.gameNum == null ? null : other.gameNum.copy();
        this.clasif = other.clasif == null ? null : other.clasif.copy();
        this.splitdeckNum = other.splitdeckNum == null ? null : other.splitdeckNum.copy();
        this.pointsA = other.pointsA == null ? null : other.pointsA.copy();
        this.pointsB = other.pointsB == null ? null : other.pointsB.copy();
        this.overA = other.overA == null ? null : other.overA.copy();
        this.overB = other.overB == null ? null : other.overB.copy();
        this.pvpA = other.pvpA == null ? null : other.pvpA.copy();
        this.pvpB = other.pvpB == null ? null : other.pvpB.copy();
        this.timeLeft = other.timeLeft == null ? null : other.timeLeft.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.teamAId = other.teamAId == null ? null : other.teamAId.copy();
        this.teamBId = other.teamBId == null ? null : other.teamBId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
    }

    @Override
    public GameCriteria copy() {
        return new GameCriteria(this);
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

    public IntegerFilter getGameNum() {
        return gameNum;
    }

    public IntegerFilter gameNum() {
        if (gameNum == null) {
            gameNum = new IntegerFilter();
        }
        return gameNum;
    }

    public void setGameNum(IntegerFilter gameNum) {
        this.gameNum = gameNum;
    }

    public StringFilter getClasif() {
        return clasif;
    }

    public StringFilter clasif() {
        if (clasif == null) {
            clasif = new StringFilter();
        }
        return clasif;
    }

    public void setClasif(StringFilter clasif) {
        this.clasif = clasif;
    }

    public IntegerFilter getSplitdeckNum() {
        return splitdeckNum;
    }

    public IntegerFilter splitdeckNum() {
        if (splitdeckNum == null) {
            splitdeckNum = new IntegerFilter();
        }
        return splitdeckNum;
    }

    public void setSplitdeckNum(IntegerFilter splitdeckNum) {
        this.splitdeckNum = splitdeckNum;
    }

    public IntegerFilter getPointsA() {
        return pointsA;
    }

    public IntegerFilter pointsA() {
        if (pointsA == null) {
            pointsA = new IntegerFilter();
        }
        return pointsA;
    }

    public void setPointsA(IntegerFilter pointsA) {
        this.pointsA = pointsA;
    }

    public IntegerFilter getPointsB() {
        return pointsB;
    }

    public IntegerFilter pointsB() {
        if (pointsB == null) {
            pointsB = new IntegerFilter();
        }
        return pointsB;
    }

    public void setPointsB(IntegerFilter pointsB) {
        this.pointsB = pointsB;
    }

    public IntegerFilter getOverA() {
        return overA;
    }

    public IntegerFilter overA() {
        if (overA == null) {
            overA = new IntegerFilter();
        }
        return overA;
    }

    public void setOverA(IntegerFilter overA) {
        this.overA = overA;
    }

    public IntegerFilter getOverB() {
        return overB;
    }

    public IntegerFilter overB() {
        if (overB == null) {
            overB = new IntegerFilter();
        }
        return overB;
    }

    public void setOverB(IntegerFilter overB) {
        this.overB = overB;
    }

    public IntegerFilter getPvpA() {
        return pvpA;
    }

    public IntegerFilter pvpA() {
        if (pvpA == null) {
            pvpA = new IntegerFilter();
        }
        return pvpA;
    }

    public void setPvpA(IntegerFilter pvpA) {
        this.pvpA = pvpA;
    }

    public IntegerFilter getPvpB() {
        return pvpB;
    }

    public IntegerFilter pvpB() {
        if (pvpB == null) {
            pvpB = new IntegerFilter();
        }
        return pvpB;
    }

    public void setPvpB(IntegerFilter pvpB) {
        this.pvpB = pvpB;
    }

    public IntegerFilter getTimeLeft() {
        return timeLeft;
    }

    public IntegerFilter timeLeft() {
        if (timeLeft == null) {
            timeLeft = new IntegerFilter();
        }
        return timeLeft;
    }

    public void setTimeLeft(IntegerFilter timeLeft) {
        this.timeLeft = timeLeft;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LongFilter getTeamAId() {
        return teamAId;
    }

    public LongFilter teamAId() {
        if (teamAId == null) {
            teamAId = new LongFilter();
        }
        return teamAId;
    }

    public void setTeamAId(LongFilter teamAId) {
        this.teamAId = teamAId;
    }

    public LongFilter getTeamBId() {
        return teamBId;
    }

    public LongFilter teamBId() {
        if (teamBId == null) {
            teamBId = new LongFilter();
        }
        return teamBId;
    }

    public void setTeamBId(LongFilter teamBId) {
        this.teamBId = teamBId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GameCriteria that = (GameCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(gameNum, that.gameNum) &&
            Objects.equals(clasif, that.clasif) &&
            Objects.equals(splitdeckNum, that.splitdeckNum) &&
            Objects.equals(pointsA, that.pointsA) &&
            Objects.equals(pointsB, that.pointsB) &&
            Objects.equals(overA, that.overA) &&
            Objects.equals(overB, that.overB) &&
            Objects.equals(pvpA, that.pvpA) &&
            Objects.equals(pvpB, that.pvpB) &&
            Objects.equals(timeLeft, that.timeLeft) &&
            Objects.equals(status, that.status) &&
            Objects.equals(teamAId, that.teamAId) &&
            Objects.equals(teamBId, that.teamBId) &&
            Objects.equals(categoryId, that.categoryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            gameNum,
            clasif,
            splitdeckNum,
            pointsA,
            pointsB,
            overA,
            overB,
            pvpA,
            pvpB,
            timeLeft,
            status,
            teamAId,
            teamBId,
            categoryId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (gameNum != null ? "gameNum=" + gameNum + ", " : "") +
            (clasif != null ? "clasif=" + clasif + ", " : "") +
            (splitdeckNum != null ? "splitdeckNum=" + splitdeckNum + ", " : "") +
            (pointsA != null ? "pointsA=" + pointsA + ", " : "") +
            (pointsB != null ? "pointsB=" + pointsB + ", " : "") +
            (overA != null ? "overA=" + overA + ", " : "") +
            (overB != null ? "overB=" + overB + ", " : "") +
            (pvpA != null ? "pvpA=" + pvpA + ", " : "") +
            (pvpB != null ? "pvpB=" + pvpB + ", " : "") +
            (timeLeft != null ? "timeLeft=" + timeLeft + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (teamAId != null ? "teamAId=" + teamAId + ", " : "") +
            (teamBId != null ? "teamBId=" + teamBId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            "}";
    }
}
