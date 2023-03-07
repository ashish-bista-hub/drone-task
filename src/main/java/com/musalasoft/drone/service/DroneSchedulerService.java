package com.musalasoft.drone.service;

import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.entity.Audit;
import com.musalasoft.drone.entity.Drone;
import com.musalasoft.drone.repository.AuditRepository;
import com.musalasoft.drone.repository.DroneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.List;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Service
public class DroneSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(DroneSchedulerService.class);

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Scheduled(fixedDelay = 10000)
    public void periodicTask() {
        log.info("------------ Running Periodic Task ------------");
        List<Drone> drones = droneRepository.findAll();
        if (CollectionUtils.isEmpty(drones)) {
            log.info("No drones are registered @ moment!!!!!");
            return;
        }
        drones.forEach(drone -> {
            Double battery = drone.getPercentage() - 2.25;
            Audit audit = new Audit();
            audit.setDrone(drone);
            audit.setInitialBattery(drone.getPercentage());
            audit.setUpdatedBattery(battery);
            audit.setAuditedOn(new Date(new java.util.Date().getTime()));
            auditRepository.save(audit);

            drone.setPercentage(battery);
            if (drone.getPercentage() < 25 && (DroneState.IDLE == drone.getState() || DroneState.LOADING == drone.getState())) {
                drone.setState(DroneState.LOADED);
                log.info("Drone battery level is below 25%, So updating state.........");
            }
            droneRepository.save(drone);
        });
    }
}
