package com.daon.airport.service;

import com.daon.airport.dao.GateFlightsDao;
import com.daon.airport.dao.GateDao;
import com.daon.airport.entity.Gate;
import com.daon.airport.entity.GateAvailability;
import com.daon.airport.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GateService {

    private final GateDao gateDao;
    private final GateFlightsDao gateFlightsDao;
    private final GateAvailabilityService gateAvailabilityService;

    public GateService(GateDao gateDao, GateFlightsDao gateFlightsDao,
                       GateAvailabilityService gateAvailabilityService) {
        this.gateDao = gateDao;
        this.gateFlightsDao = gateFlightsDao;
        this.gateAvailabilityService = gateAvailabilityService;
    }

    public Gate getGateByFlightId(Long flightId) {
        return Optional.ofNullable(gateFlightsDao.getGateIdForFlight(flightId))
                .orElseThrow(() -> new NotFoundException("No gate found for given flight!"));
    }

    public boolean isGateAvailable(Long gateId, Date from, Date to) {
        Gate gate = getGateById(gateId);
        List<GateAvailability> gateAvailabilityForDate =
                gateAvailabilityService.getGateAvailabilityForDate(gate, from, to);

        if (!gateAvailabilityForDate.isEmpty()) {
            return true;
        }

        return gateFlightsDao.isGateAvailable(gate.getId(), from, to);
    }

    public Gate getGateById(Long gateId) {
        return gateDao.findById(gateId)
                .orElseThrow(() -> new NotFoundException("No gate found for given id!"));
    }

}
