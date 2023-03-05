package com.musalasoft.drone.service;

import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.dto.DroneDto;
import com.musalasoft.drone.dto.MedicationDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
public interface DroneService {

    DroneDto registerDrone(DroneDto droneDto);

    DroneDto loadMedications(Long droneId, MultipartFile image, MedicationDto medicationDto) throws IOException;

    List<DroneDto> getAllDronesByState(DroneState droneState);

    List<MedicationDto> getAllMedications(Long droneId);

    List<MedicationDto> getAllMedications(String serialNo);
}
