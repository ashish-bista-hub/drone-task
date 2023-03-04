package com.musalasoft.drone.repository;

import com.musalasoft.drone.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByDroneId(Long droneId);
}
