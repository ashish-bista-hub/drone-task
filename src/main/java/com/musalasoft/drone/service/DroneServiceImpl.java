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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
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
        drone.setState(DroneState.IDLE);
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
        if (DroneState.IDLE != drone.getState() && DroneState.LOADING != drone.getState()) {
            throw new DroneException("Drone must be in the Idle or Loading state to perform this action.");
        }
        if (!StringUtils.hasLength(medicationDto.getName()) || !medicationDto.getName().matches("[0-9A-Za-z\\-]+")) {
            throw new DroneException("Medication name is mandatory and allowed only letters, numbers and '-', '_'");
        }
        if (!StringUtils.hasLength(medicationDto.getCode()) || !medicationDto.getCode().matches("[0-9A-Z\\-]+")) {
            throw new DroneException("Medication code is mandatory and allowed only upper case letters, numbers and '-', '_'");
        }
        List<Medication> medications = medicationRepository.findByDroneId(drone.getId());
        if (!CollectionUtils.isEmpty(medications)) {
            double loadedMedicationWeight = medications.stream().filter(m -> m.getWeight() != null && m.getWeight() > 0)
                    .mapToDouble(Medication::getWeight).sum();
            double newMedicationWeight = loadedMedicationWeight + medicationDto.getWeight();
            if (drone.getWeight() != null && newMedicationWeight > drone.getWeight()) {
                throw new DroneException("Drone " + drone.getSerialNo() +
                        " weight will be exceed by current loading, so further loading is not allowed.");
            } else if (drone.getWeight() != null && newMedicationWeight == drone.getWeight()) {
                drone.setState(DroneState.LOADED);
            } else {
                drone.setState(DroneState.LOADING);
            }
            drone = droneRepository.save(drone);
        }

        Medication medication = new Medication();
        medication.setName(medicationDto.getName());
        medication.setCode(medicationDto.getCode());
        medication.setDrone(drone);
        medication.setImage(image.getBytes());
        medication.setWeight(medicationDto.getWeight());
        medication.setImageName(image.getOriginalFilename());
        medication = medicationRepository.saveAndFlush(medication);

        medicationDto.setId(medication.getId());
        medicationDto.setImage(medication.getImage());
        medicationDto.setImageName(medication.getImageName());

        DroneDto dto = new DroneDto();
        dto.setId(drone.getId());
        dto.setSerialNo(drone.getSerialNo());
        dto.setDroneModel(drone.getModel());
        dto.setDroneState(drone.getState());
        dto.setWeight(drone.getWeight());
        dto.setBatteryCapacity(drone.getPercentage());
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

    @Override
    public List<MedicationDto> getAllMedications(String serialNo) {
        Drone drone = droneRepository.findDroneBySerialNo(serialNo);
        if (drone == null) {
            return Collections.emptyList();
        }
        return getAllMedications(drone.getId());
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
