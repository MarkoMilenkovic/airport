package com.daon.airport.dao;

import com.daon.airport.entity.Gate;
import com.daon.airport.entity.GateFlights;
import com.daon.airport.entity.GateFlightsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GateFlightsDao extends JpaRepository<GateFlights, GateFlightsId> {

    @Query("select g.gate from GateFlights g where g.flight.id = :flightId ")
    Gate getGateForFlight(@Param("flightId") Long flightId);

    @Query("select count(g) = 0 from GateFlights g where g.gate.id = :gateId and g.isTerminated = false " +
            "and (g.gateLockedUntil BETWEEN :from AND :to)")
    boolean isGateAvailable(@Param("gateId") Long gateId, @Param("from") Date from, @Param("to") Date to);

    @Query("select g from GateFlights g where g.gate.id = :gateId and g.isTerminated = false " +
            "and (g.gateLockedUntil BETWEEN :from AND :to)")
    List<GateFlights> getBusyGate(@Param("gateId") Long gateId, @Param("from") Date from, @Param("to") Date toe);

}
