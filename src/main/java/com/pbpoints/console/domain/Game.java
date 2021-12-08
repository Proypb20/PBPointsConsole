package com.pbpoints.console.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pbpoints.console.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_num")
    private Integer gameNum;

    @Column(name = "clasif")
    private String clasif;

    @Column(name = "splitdeck_num")
    private Integer splitdeckNum;

    @Column(name = "points_a")
    private Integer pointsA;

    @Column(name = "points_b")
    private Integer pointsB;

    @Column(name = "over_a")
    private Integer overA;

    @Column(name = "over_b")
    private Integer overB;

    @Column(name = "pvp_a")
    private Integer pvpA;

    @Column(name = "pvp_b")
    private Integer pvpB;

    @Column(name = "time_left")
    private Integer timeLeft;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    private Team teamA;

    @ManyToOne(optional = false)
    @NotNull
    private Team teamB;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getGameNum() {
        return this.gameNum;
    }

    public Game gameNum(Integer gameNum) {
        this.gameNum = gameNum;
        return this;
    }

    public void setGameNum(Integer gameNum) {
        this.gameNum = gameNum;
    }

    public String getClasif() {
        return this.clasif;
    }

    public Game clasif(String clasif) {
        this.clasif = clasif;
        return this;
    }

    public void setClasif(String clasif) {
        this.clasif = clasif;
    }

    public Integer getSplitdeckNum() {
        return this.splitdeckNum;
    }

    public Game splitdeckNum(Integer splitdeckNum) {
        this.splitdeckNum = splitdeckNum;
        return this;
    }

    public void setSplitdeckNum(Integer splitdeckNum) {
        this.splitdeckNum = splitdeckNum;
    }

    public Integer getPointsA() {
        return this.pointsA;
    }

    public Game pointsA(Integer pointsA) {
        this.pointsA = pointsA;
        return this;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Integer getPointsB() {
        return this.pointsB;
    }

    public Game pointsB(Integer pointsB) {
        this.pointsB = pointsB;
        return this;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Integer getOverA() {
        return this.overA;
    }

    public Game overA(Integer overA) {
        this.overA = overA;
        return this;
    }

    public void setOverA(Integer overA) {
        this.overA = overA;
    }

    public Integer getOverB() {
        return this.overB;
    }

    public Game overB(Integer overB) {
        this.overB = overB;
        return this;
    }

    public void setOverB(Integer overB) {
        this.overB = overB;
    }

    public Integer getPvpA() {
        return this.pvpA;
    }

    public Game pvpA(Integer pvpA) {
        this.pvpA = pvpA;
        return this;
    }

    public void setPvpA(Integer pvpA) {
        this.pvpA = pvpA;
    }

    public Integer getPvpB() {
        return this.pvpB;
    }

    public Game pvpB(Integer pvpB) {
        this.pvpB = pvpB;
        return this;
    }

    public void setPvpB(Integer pvpB) {
        this.pvpB = pvpB;
    }

    public Integer getTimeLeft() {
        return this.timeLeft;
    }

    public Game timeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
        return this;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Status getStatus() {
        return this.status;
    }

    public Game status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Team getTeamA() {
        return this.teamA;
    }

    public Game teamA(Team team) {
        this.setTeamA(team);
        return this;
    }

    public void setTeamA(Team team) {
        this.teamA = team;
    }

    public Team getTeamB() {
        return this.teamB;
    }

    public Game teamB(Team team) {
        this.setTeamB(team);
        return this;
    }

    public void setTeamB(Team team) {
        this.teamB = team;
    }

    public Category getCategory() {
        return this.category;
    }

    public Game category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
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
            ", category='" + getCategory() + "'" +
            "}";
    }
}
