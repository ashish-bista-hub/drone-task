package com.musalasoft.drone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.dto.DroneDto;
import com.musalasoft.drone.dto.MedicationDto;
import com.musalasoft.drone.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@RestController
@RequestMapping("/v1/drone")
public class DroneRestController {

    @Autowired
    private DroneService droneService;

    @PostMapping("/register")
    public ResponseEntity<DroneDto> register(@RequestBody DroneDto droneDto) {
        return new ResponseEntity<>(droneService.registerDrone(droneDto), HttpStatus.CREATED);
    }

    @PostMapping("/{droneId}/load")
    public ResponseEntity<DroneDto> load(@PathVariable(value = "droneId") Long droneId,
                                         @RequestParam("image") MultipartFile image,
                                         @RequestParam("medication") String medication) throws IOException {
        MedicationDto medicationDto = new ObjectMapper().readValue(medication, MedicationDto.class);
        return new ResponseEntity<>(droneService.loadMedications(droneId, image, medicationDto), HttpStatus.CREATED);
    }

    @GetMapping("/{droneState}/state")
    public ResponseEntity<List<DroneDto>> getDronesByState(@PathVariable("droneState") DroneState droneState) {
        return new ResponseEntity<>(droneService.getAllDronesByState(droneState), HttpStatus.OK);
    }

    @GetMapping("/{droneId}/medications")
    public ResponseEntity<List<MedicationDto>> getAllMedicationsForDrone(@PathVariable(value = "droneId") Long droneId) {
        return new ResponseEntity<>(droneService.getAllMedications(droneId), HttpStatus.OK);
    }
}
