package com.musalasoft.drone.repository;

import com.musalasoft.drone.constants.DroneState;
import com.musalasoft.drone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    List<Drone> findDronesByState(DroneState droneState);

    Drone findDroneBySerialNo(String serialNo);
}
