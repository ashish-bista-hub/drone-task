package com.musalasoft.drone.service;

import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.dto.DroneDto;
import com.musalasoft.drone.dto.MedicationDto;
import com.musalasoft.drone.entity.Drone;
import com.musalasoft.drone.entity.Medication;
import com.musalasoft.drone.exception.DroneException;
import com.musalasoft.drone.repository.DroneRepository;
import com.musalasoft.drone.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Service
public class DroneServiceImpl implements DroneService {

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/drone";

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    public DroneDto registerDrone(DroneDto droneDto) {
        Drone drone = new Drone();
        drone.setSerialNo(droneDto.getSerialNo());
        drone.setModel(droneDto.getDroneModel());
        drone.setWeight(droneDto.getWeight());
        drone.setState(droneDto.getDroneState());
        drone.setPercentage(droneDto.getBatteryCapacity());
        drone = droneRepository.saveAndFlush(drone);
        droneDto.setId(drone.getId());
        return droneDto;
    }

    @Override
    public DroneDto loadMedications(Long droneId, MultipartFile image, MedicationDto medicationDto) throws IOException {
        Optional<Drone> droneOpt = droneRepository.findById(droneId);
        if (!droneOpt.isPresent()) {
            throw new DroneException("Drone not found with id = " + droneId);
        }
        if (!validateImageFile(image)) {
            throw new DroneException("Image file should belong to one of the types [BMP, GIF, JPEG, PNG, TIFF, WBMP].");
        }
        Drone drone = droneOpt.get();
        drone.setState(DroneState.LOADING);
        drone = droneRepository.save(drone);

        DroneDto dto = new DroneDto();
        dto.setId(drone.getId());
        dto.setSerialNo(drone.getSerialNo());
        dto.setDroneModel(drone.getModel());
        dto.setDroneState(drone.getState());
        dto.setWeight(drone.getWeight());
        dto.setBatteryCapacity(drone.getPercentage());

        Medication medication = new Medication();
        medication.setCode(medicationDto.getCode());
        medication.setDrone(drone);
        medication.setImage(image.getBytes());
        medication.setWeight(medicationDto.getWeight());
        medication.setImageName(image.getOriginalFilename());
        medication = medicationRepository.saveAndFlush(medication);

        medicationDto.setId(medication.getId());
        medicationDto.setImage(medication.getImage());
        medicationDto.setImageName(medication.getImageName());
        dto.getMedications().add(medicationDto);
        return dto;
    }

    @Override
    public List<DroneDto> getAllDronesByState(DroneState droneState) {
        return droneRepository.findDronesByState(droneState).stream()
                .map(drone -> {
                    DroneDto droneDto = new DroneDto();
                    droneDto.setId(drone.getId());
                    droneDto.setSerialNo(drone.getSerialNo());
                    droneDto.setBatteryCapacity(drone.getPercentage());
                    droneDto.setDroneModel(drone.getModel());
                    droneDto.setWeight(drone.getWeight());
                    return droneDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<MedicationDto> getAllMedications(Long droneId) {
        return medicationRepository.findByDroneId(droneId).stream()
                .map(medication -> {
                    MedicationDto medicationDto = new MedicationDto();
                    medicationDto.setId(medication.getId());
                    medicationDto.setImageName(medication.getImageName());
                    medicationDto.setImage(medication.getImage());
                    medicationDto.setWeight(medication.getWeight());
                    return medicationDto;
                }).collect(Collectors.toList());
    }

    private boolean validateImageFile(MultipartFile image) {
        try (InputStream input = image.getInputStream()) {
            try {
                ImageIO.read(input).toString();
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
}
