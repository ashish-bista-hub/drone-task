package com.musalasoft.drone.dto;

import com.musalasoft.drone.constants.DroneModel;
import com.musalasoft.drone.constants.DroneState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
public class DroneDto {

    private Long id;
    private String serialNo;
    private DroneModel droneModel;
    private Double weight;
    private Double batteryCapacity;
    private DroneState droneState;
    private List<MedicationDto> medications = new ArrayList<>();

    public DroneDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public DroneModel getDroneModel() {
        return droneModel;
    }

    public void setDroneModel(DroneModel droneModel) {
        this.droneModel = droneModel;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public DroneState getDroneState() {
        return droneState;
    }

    public void setDroneState(DroneState droneState) {
        this.droneState = droneState;
    }

    public List<MedicationDto> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationDto> medications) {
        this.medications = medications;
    }

    @Override
    public String toString() {
        return "DroneDto{" +
                "id=" + id +
                ", serialNo='" + serialNo + '\'' +
                ", droneModel=" + droneModel +
                ", weight=" + weight +
                ", batteryCapacity=" + batteryCapacity +
                ", droneState=" + droneState +
                ", medications=" + medications +
                '}';
    }
}
