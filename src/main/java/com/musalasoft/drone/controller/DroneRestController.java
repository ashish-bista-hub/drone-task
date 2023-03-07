package com.musalasoft.drone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.dto.AuditDto;
import com.musalasoft.drone.dto.DroneDto;
import com.musalasoft.drone.dto.MedicationDto;
import com.musalasoft.drone.service.DroneService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@RestController
@RequestMapping(DroneRestController.BASE_ENDPOINT)
public class DroneRestController {

    public static final String BASE_ENDPOINT = "/api/v1/drone";

    @Autowired
    private DroneService droneService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Register a drone", response = DroneDto.class,
            notes = "This API will register a drone and returns registered drone info.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 404, message = "The resource not found")
    })
    public ResponseEntity<DroneDto> register(@ApiParam(name = "droneDto",
            value = "{\"serialNo\":\"Drone-MKYU-0L\",\"droneModel\":\"LIGHT_WEIGHT\",\"weight\":200,\"batteryCapacity\":95.67}"
    ) @RequestBody DroneDto droneDto) {
        return new ResponseEntity<>(droneService.registerDrone(droneDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{droneId}/load", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Load a drone with medication items", response = DroneDto.class,
            notes = "This API will load a drone with medication items.")
    public ResponseEntity<DroneDto> load(@PathVariable(value = "droneId") Long droneId,
                                         @ApiParam(name = "image", value = "Image file of type PNG, JPEG, JPG etc.") @RequestParam("image") MultipartFile image,
                                         @ApiParam(name = "medication", value = "String representation of medication dto") @RequestParam("medication") String medication) throws IOException {
        MedicationDto medicationDto = new ObjectMapper().readValue(medication, MedicationDto.class);
        return new ResponseEntity<>(droneService.loadMedications(droneId, image, medicationDto), HttpStatus.CREATED);
    }

    @GetMapping("/{droneState}/state")
    @ApiOperation(value = "Check available drones by state",
            notes = "This API allows to check drone by state. For e.g. user can check available drones for loading")
    public ResponseEntity<List<DroneDto>> getDronesByState(@PathVariable("droneState") DroneState droneState) {
        return new ResponseEntity<>(droneService.getAllDronesByState(droneState), HttpStatus.OK);
    }

    @GetMapping("/idle")
    @ApiOperation(value = "Check available drones for loading",
            notes = "This API allows to check available drones for loading.")
    public ResponseEntity<List<DroneDto>> getDronesByState() {
        return new ResponseEntity<>(droneService.getAllDronesByState(DroneState.IDLE), HttpStatus.OK);
    }

    @GetMapping("/{droneId}/medications")
    @ApiOperation(value = "Check loaded medication items for a given drone by Id",
            notes = "This API allows to check loaded medication items for a given drone by Id")
    public ResponseEntity<List<MedicationDto>> getAllMedicationsForDrone(@PathVariable(value = "droneId") Long droneId) {
        return new ResponseEntity<>(droneService.getAllMedications(droneId), HttpStatus.OK);
    }

    @GetMapping("/medications")
    @ApiOperation(value = "Check loaded medication items for a given drone by serial no",
            notes = "This API allows to check loaded medication items for a given drone by serial no.")
    public ResponseEntity<List<MedicationDto>> getAllMedicationsForDrone(@RequestParam(value = "serialNo") String serialNo) {
        return new ResponseEntity<>(droneService.getAllMedications(serialNo), HttpStatus.OK);
    }

    @GetMapping("/{droneId}/audits")
    @ApiOperation(value = "Check drone audit logs by Id",
            notes = "This API allows to check audit logs for drone by Id")
    public ResponseEntity<Map<DroneDto, List<AuditDto>>> getAllAuditsForDrone(@PathVariable(value = "droneId") Long droneId) {
        return new ResponseEntity<>(droneService.getAllAudits(droneId), HttpStatus.OK);
    }
}
