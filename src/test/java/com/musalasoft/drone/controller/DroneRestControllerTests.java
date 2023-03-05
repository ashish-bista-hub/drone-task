package com.musalasoft.drone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.drone.constants.DroneModel;
import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.dto.DroneDto;
import com.musalasoft.drone.dto.MedicationDto;
import com.musalasoft.drone.service.DroneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DroneRestController.class)
public class DroneRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterDrone() throws Exception {
        when(droneService.registerDrone(any(DroneDto.class))).thenReturn(getDrone());

        this.mockMvc.perform(post(DroneRestController.BASE_ENDPOINT + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDrone())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.serialNo").value("TEST-DRONE-23675890-12679"))
                .andExpect(jsonPath("$.droneModel").value("HEAVY_WEIGHT"))
                .andExpect(jsonPath("$.weight").value(28D))
                .andExpect(jsonPath("$.droneState").value("IDLE"))
                .andExpect(jsonPath("$.batteryCapacity").value(90D));
    }

    @Test
    public void testGetDronesByState() throws Exception {
        when(droneService.getAllDronesByState(any(DroneState.class))).thenReturn(Arrays.asList(getDrone()));

        this.mockMvc.perform(get(DroneRestController.BASE_ENDPOINT + "/" + DroneState.IDLE + "/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].serialNo").value("TEST-DRONE-23675890-12679"))
                .andExpect(jsonPath("$[0].droneState").value("IDLE"));
    }

    @Test
    public void testDroneAvailableForLoading() throws Exception {
        when(droneService.getAllDronesByState(any(DroneState.class))).thenReturn(Arrays.asList(getDrone()));

        this.mockMvc.perform(get(DroneRestController.BASE_ENDPOINT + "/idle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].serialNo").value("TEST-DRONE-23675890-12679"))
                .andExpect(jsonPath("$[0].droneState").value("IDLE"));
    }

    @Test
    public void testLoadedMedicationsForDroneById() throws Exception {
        List<MedicationDto> medications = new ArrayList<>();
        medications.add(getMedications(1L, "Med-Name-1", "test1.png", 25D, "Test-Med-1", "This is a test image 1".getBytes()));
        medications.add(getMedications(2L, "Med-Name-2", "test2.png", 100D, "Test-Med-2", "This is a test image 2".getBytes()));
        when(droneService.getAllMedications(anyLong())).thenReturn(medications);

        this.mockMvc.perform(get(DroneRestController.BASE_ENDPOINT + "/" + 1L + "/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Med-Name-1"))
                .andExpect(jsonPath("$[0].weight").value(25D))
                .andExpect(jsonPath("$[0].code").value("Test-Med-1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].code").value("Test-Med-2"));
    }

    @Test
    public void testLoadedMedicationsForDroneBySerialNo() throws Exception {
        List<MedicationDto> medications = new ArrayList<>();
        medications.add(getMedications(1L, "Med-Name-1", "test1.png", 25D, "Test-Med-1", "This is a test image 1".getBytes()));
        medications.add(getMedications(2L, "Med-Name-2", "test2.png", 100D, "Test-Med-2", "This is a test image 2".getBytes()));
        when(droneService.getAllMedications(anyString())).thenReturn(medications);

        this.mockMvc.perform(get(DroneRestController.BASE_ENDPOINT + "/medications?serialNo=TEST-DRONE-23675890-12679"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Med-Name-1"))
                .andExpect(jsonPath("$[0].weight").value(25D))
                .andExpect(jsonPath("$[0].code").value("Test-Med-1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].code").value("Test-Med-2"));
    }

    @Test
    public void testLoadMedication() throws Exception {
        MockMultipartFile med = new MockMultipartFile("med-bottle", "med-bottle.png", "image/png", "Test Image".getBytes());
        MedicationDto medicationDto = getMedications(1L, "Med-Name-1", "med-bottle.png", 25D, "Test-Med-1", med.getBytes());
        DroneDto droneDto = getDrone();
        droneDto.getMedications().add(medicationDto);

        when(droneService.loadMedications(anyLong(), any(MultipartFile.class), any(MedicationDto.class))).thenReturn(droneDto);
        this.mockMvc.perform(MockMvcRequestBuilders.multipart(DroneRestController.BASE_ENDPOINT + "/" + 1L + "/load")
                        .file("image", med.getBytes())
                        .param("medication", objectMapper.writeValueAsString(medicationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.serialNo").value("TEST-DRONE-23675890-12679"))
                .andExpect(jsonPath("$.droneModel").value("HEAVY_WEIGHT"))
                .andExpect(jsonPath("$.weight").value(28D))
                .andExpect(jsonPath("$.droneState").value("IDLE"))
                .andExpect(jsonPath("$.batteryCapacity").value(90D))
                .andExpect(jsonPath("$.medications", hasSize(1)))
                .andExpect(jsonPath("$.medications[0].imageName").value("med-bottle.png"));
    }

    private MedicationDto getMedications(Long id, String name, String imageName, Double weight, String code, byte[] image) {
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setId(id);
        medicationDto.setName(name);
        medicationDto.setImageName(imageName);
        medicationDto.setWeight(weight);
        medicationDto.setCode(code);
        medicationDto.setImage(image);
        return medicationDto;
    }

    private DroneDto getDrone() {
        DroneDto dto = new DroneDto();
        dto.setId(1L);
        dto.setDroneModel(DroneModel.HEAVY_WEIGHT);
        dto.setDroneState(DroneState.IDLE);
        dto.setSerialNo("TEST-DRONE-23675890-12679");
        dto.setWeight(28D);
        dto.setBatteryCapacity(90D);
        return dto;
    }
}
