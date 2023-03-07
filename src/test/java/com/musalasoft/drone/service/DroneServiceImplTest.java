package com.musalasoft.drone.service;

import com.musalasoft.drone.constants.DroneModel;
import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.dto.AuditDto;
import com.musalasoft.drone.dto.DroneDto;
import com.musalasoft.drone.dto.MedicationDto;
import com.musalasoft.drone.entity.Audit;
import com.musalasoft.drone.entity.Drone;
import com.musalasoft.drone.entity.Medication;
import com.musalasoft.drone.repository.AuditRepository;
import com.musalasoft.drone.repository.DroneRepository;
import com.musalasoft.drone.repository.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@SpringBootTest
class DroneServiceImplTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private DroneService droneService = new DroneServiceImpl();

    @Test
    void registerDrone() {
        when(droneRepository.saveAndFlush(any(Drone.class))).thenReturn(getDrone());
        DroneDto dto = droneService.registerDrone(getDroneDto());
        assertNotNull(dto);
        assertEquals(dto.getId(), 1L);
        verify(droneRepository, times(1)).saveAndFlush(any(Drone.class));
    }

    @Test
    void loadMedications() throws IOException {
        Drone drone = getDrone();
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(medicationRepository.findByDroneId(1L)).thenReturn(Collections.singletonList(getMedication()));
        Medication medication = new Medication();
        medication.setId(2L);
        medication.setCode("TEST-MED-UYT");
        medication.setName("Test-Med-UYT-098");
        medication.setWeight(20D);
        medication.setImageName("med-bottle.png");
        medication.setImage("This is a test image file".getBytes());
        when(medicationRepository.saveAndFlush(any(Medication.class))).thenReturn(medication);
        drone.setState(DroneState.LOADING);
        when(droneRepository.save(any(Drone.class))).thenReturn(drone);
        final MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("med-bottle.png");
        when(mockFile.getBytes()).thenReturn("This is a test image file".getBytes());
        when(mockFile.getInputStream()).thenReturn(new FileInputStream(ResourceUtils.getFile("classpath:med-bottle.png")));
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setCode("TEST-MED-UYT");
        medicationDto.setWeight(20D);
        medicationDto.setName("Test-Med-UYT-098");
        DroneDto dto = droneService.loadMedications(1L, mockFile, medicationDto);
        assertNotNull(dto);
        assertEquals(dto.getDroneState(), DroneState.LOADING);
        assertEquals(dto.getMedications().size(), 1);
        assertEquals(dto.getMedications().get(0).getImageName(), "med-bottle.png");
        verify(droneRepository, times(1)).findById(1L);
        verify(medicationRepository, times(1)).findByDroneId(1L);
        verify(droneRepository, times(1)).save(any(Drone.class));
        verify(medicationRepository, times(1)).saveAndFlush(any(Medication.class));
    }

    @Test
    void getAllDronesByState() {
        when(droneRepository.findDronesByState(any(DroneState.class))).thenReturn(Collections.singletonList(getDrone()));
        List<DroneDto> droneDtos = droneService.getAllDronesByState(DroneState.IDLE);
        assertNotNull(droneDtos);
        assertEquals(droneDtos.size(), 1);
        assertEquals(droneDtos.get(0).getDroneState(), DroneState.IDLE);
        verify(droneRepository, times(1)).findDronesByState(any(DroneState.class));
    }

    @Test
    void getAllMedications() {
        when(medicationRepository.findByDroneId(1L)).thenReturn(Collections.singletonList(getMedication()));
        List<MedicationDto> medicationDtos = droneService.getAllMedications(1L);
        assertNotNull(medicationDtos);
        assertEquals(medicationDtos.size(), 1);
        assertEquals(medicationDtos.get(0).getName(), "Test-Med-UYT-098");
        assertEquals(medicationDtos.get(0).getCode(), "Test-Med-ERQW-NBV");
        verify(medicationRepository, times(1)).findByDroneId(1L);
    }

    @Test
    void testGetAllMedications() {
        when(droneRepository.findDroneBySerialNo("TEST-DRONE-23675890-12679")).thenReturn(getDrone());
        when(medicationRepository.findByDroneId(1L)).thenReturn(Collections.singletonList(getMedication()));
        List<MedicationDto> medicationDtos = droneService.getAllMedications("TEST-DRONE-23675890-12679");
        assertNotNull(medicationDtos);
        assertEquals(medicationDtos.size(), 1);
        assertEquals(medicationDtos.get(0).getName(), "Test-Med-UYT-098");
        assertEquals(medicationDtos.get(0).getCode(), "Test-Med-ERQW-NBV");
        verify(medicationRepository, times(1)).findByDroneId(1L);
        verify(droneRepository, times(1)).findDroneBySerialNo("TEST-DRONE-23675890-12679");
    }

    @Test
    void getAllAudits() {
        when(auditRepository.findByDroneIdOrderByAuditedOnDesc(1L)).thenReturn(Collections.singletonList(getAudit()));
        Map<DroneDto, List<AuditDto>> audits = droneService.getAllAudits(1L);
        assertNotNull(audits);
        assertEquals(audits.size(), 1);
        verify(auditRepository, times(1)).findByDroneIdOrderByAuditedOnDesc(1L);
    }

    private Audit getAudit() {
        Audit audit = new Audit();
        audit.setId(1L);
        audit.setAuditedOn(new Date(new java.util.Date().getTime()));
        audit.setInitialBattery(2D);
        audit.setUpdatedBattery(1D);
        audit.setDrone(getDrone());
        return audit;
    }

    private DroneDto getDroneDto() {
        DroneDto dto = new DroneDto();
        dto.setDroneModel(DroneModel.HEAVY_WEIGHT);
        dto.setDroneState(DroneState.IDLE);
        dto.setSerialNo("TEST-DRONE-23675890-12679");
        dto.setWeight(28D);
        dto.setBatteryCapacity(90D);
        return dto;
    }

    private Drone getDrone() {
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setModel(DroneModel.HEAVY_WEIGHT);
        drone.setState(DroneState.IDLE);
        drone.setSerialNo("TEST-DRONE-23675890-12679");
        drone.setWeight(28D);
        drone.setPercentage(90D);
        return drone;
    }

    private Medication getMedication() {
        Medication medication = new Medication();
        medication.setId(1L);
        medication.setDrone(getDrone());
        medication.setName("Test-Med-UYT-098");
        medication.setCode("Test-Med-ERQW-NBV");
        medication.setImageName("med-gr.png");
        medication.setImage("This is a test image".getBytes());
        return medication;
    }
}