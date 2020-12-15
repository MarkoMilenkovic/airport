package com.daon.airport.dao;

import com.daon.airport.entity.GateAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface GateAvailabilityDao extends JpaRepository<GateAvailability, Long> {

    @Query("select ga from GateAvailability ga where ga.gate.id = :gateId " +
            "and ga.availableFrom > :from and ga.availableTo < :to")
    List<GateAvailability> getByGateIdAndDateBetween(@Param("gateId") Long gateId,
                                                     @Param("from") Date from, @Param("to") Date to);

}
