package com.musalasoft.drone.repository;

import com.musalasoft.drone.entity.Audit;
import com.musalasoft.drone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    List<Audit> findByDroneIdOrderByAuditedOnDesc(Long droneId);
}
