package com.pbpoints.console.service.dto;

import com.pbpoints.console.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.console.domain.Game} entity.
 */
public class GameDTO implements Serializable {

    private Long id;

    private Integer gameNum;

    private String clasif;

    private Integer splitdeckNum;

    private Integer pointsA;

    private Integer pointsB;

    private Integer overA;

    private Integer overB;

    private Integer pvpA;

    private Integer pvpB;

    private Integer timeLeft;

    private Status status;

    private TeamDTO teamA;

    private TeamDTO teamB;

    private CategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGameNum() {
        return gameNum;
    }

    public void setGameNum(Integer gameNum) {
        this.gameNum = gameNum;
    }

    public String getClasif() {
        return clasif;
    }

    public void setClasif(String clasif) {
        this.clasif = clasif;
    }

    public Integer getSplitdeckNum() {
        return splitdeckNum;
    }

    public void setSplitdeckNum(Integer splitdeckNum) {
        this.splitdeckNum = splitdeckNum;
    }

    public Integer getPointsA() {
        return pointsA;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Integer getPointsB() {
        return pointsB;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Integer getOverA() {
        return overA;
    }

    public void setOverA(Integer overA) {
        this.overA = overA;
    }

    public Integer getOverB() {
        return overB;
    }

    public void setOverB(Integer overB) {
        this.overB = overB;
    }

    public Integer getPvpA() {
        return pvpA;
    }

    public void setPvpA(Integer pvpA) {
        this.pvpA = pvpA;
    }

    public Integer getPvpB() {
        return pvpB;
    }

    public void setPvpB(Integer pvpB) {
        this.pvpB = pvpB;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TeamDTO getTeamA() {
        return teamA;
    }

    public void setTeamA(TeamDTO teamA) {
        this.teamA = teamA;
    }

    public TeamDTO getTeamB() {
        return teamB;
    }

    public void setTeamB(TeamDTO teamB) {
        this.teamB = teamB;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameDTO)) {
            return false;
        }

        GameDTO gameDTO = (GameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameDTO{" +
            "id=" + getId() +
            ", gameNum=" + getGameNum() +
            ", clasif='" + getClasif() + "'" +
            ", splitdeckNum=" + getSplitdeckNum() +
            ", pointsA=" + getPointsA() +
            ", pointsB=" + getPointsB() +
            ", overA=" + getOverA() +
            ", overB=" + getOverB() +
            ", pvpA=" + getPvpA() +
            ", pvpB=" + getPvpB() +
            ", timeLeft=" + getTimeLeft() +
            ", status='" + getStatus() + "'" +
            ", teamA=" + getTeamA() +
            ", teamB=" + getTeamB() +
            ", category=" + getCategory() +
            "}";
    }
}
