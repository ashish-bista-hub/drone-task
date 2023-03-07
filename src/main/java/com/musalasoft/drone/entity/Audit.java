package com.musalasoft.drone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Entity
@Table(name = "audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drone_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Drone drone;

    @Column(name = "initial_battery")
    private Double initialBattery;

    @Column(name = "updated_battery")
    private Double updatedBattery;

    @Column(name = "audited_on")
    private Date auditedOn;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Double getInitialBattery() {
        return initialBattery;
    }

    public void setInitialBattery(Double initialBattery) {
        this.initialBattery = initialBattery;
    }

    public Double getUpdatedBattery() {
        return updatedBattery;
    }

    public void setUpdatedBattery(Double updatedBattery) {
        this.updatedBattery = updatedBattery;
    }

    public Date getAuditedOn() {
        return auditedOn;
    }

    public void setAuditedOn(Date auditedOn) {
        this.auditedOn = auditedOn;
    }
}
