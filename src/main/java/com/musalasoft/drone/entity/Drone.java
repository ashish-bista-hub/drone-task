package com.musalasoft.drone.entity;

import com.musalasoft.drone.constants.DroneModel;
import com.musalasoft.drone.constants.DroneState;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Entity
@Table(name = "drone")
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serial_no", nullable = false, length = 100, unique = true)
    @NotBlank(message = "Drone serial number cannot be blank")
    @Length(min = 15, max = 100, message = "Drone serial number must be between 15-100 characters")
    private String serialNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private DroneModel model;

    @Column(name = "weight")
    @Max(value = 500, message = "Drone weight must be less than or equal to 500gr")
    private Double weight;

    @Column(name = "percentage")
    private Double percentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private DroneState state;

    public Drone() {
    }

    public Drone(String serialNo, DroneModel model, Double weight) {
        this.serialNo = serialNo;
        this.model = model;
        this.weight = weight;
        this.state = DroneState.IDLE;
        this.percentage = 99D;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public DroneModel getModel() {
        return model;
    }

    public void setModel(DroneModel model) {
        this.model = model;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public DroneState getState() {
        return state;
    }

    public void setState(DroneState state) {
        this.state = state;
    }
}
