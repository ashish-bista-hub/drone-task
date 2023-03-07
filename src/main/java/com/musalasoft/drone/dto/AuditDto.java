package com.musalasoft.drone.dto;

import java.sql.Date;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
public class AuditDto {

    private Long id;

    private Double initialBattery;

    private Double updatedBattery;

    private Date auditedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
