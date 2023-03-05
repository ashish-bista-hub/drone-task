package com.musalasoft.drone.dto;

import java.util.Arrays;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
public class MedicationDto {

    private Long id;
    private String name;
    private Double weight;
    private String code;
    private byte[] image;
    private String imageName;

    public MedicationDto() {
    }

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "MedicationDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", code='" + code + '\'' +
                ", image=" + Arrays.toString(image) +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
