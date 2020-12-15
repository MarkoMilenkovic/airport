package com.daon.airport.service;

import com.daon.airport.dao.GateAvailabilityDao;
import com.daon.airport.dao.GateFlightsDao;
import com.daon.airport.entity.Gate;
import com.daon.airport.entity.GateAvailability;
import com.daon.airport.entity.GateFlights;
import com.daon.airport.exception.BadRequestException;
import com.daon.airport.model.GateAvailabilityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class GateAvailabilityService {

    private final GateAvailabilityDao gateAvailabilityDao;
    private final GateFlightsDao gateFlightsDao;

    public GateAvailabilityService(GateAvailabilityDao gateAvailabilityDao, GateFlightsDao gateFlightsDao) {
        this.gateAvailabilityDao = gateAvailabilityDao;
        this.gateFlightsDao = gateFlightsDao;
    }

    public List<GateAvailability> getGateAvailabilityForDate(Gate gate, Date from, Date to) {
        return gateAvailabilityDao.getByGateIdAndDateBetween(gate.getId(), from, to);
    }

    @Transactional
    public void markGateAsAvailable(Gate gate, Date from, Date to) {
        List<GateFlights> busyGates = gateFlightsDao.getBusyGate(gate.getId(), from, to);
        if (busyGates.isEmpty()) {
            throw new BadRequestException("Gate is already available!");
        }
        busyGates.forEach(busyGate -> busyGate.setIsTerminated(true));
    }

    @Transactional
    public void removeGateAvailability(Long gateAvailabilityId) {
        gateAvailabilityDao.deleteById(gateAvailabilityId);
    }

    @Transactional
    public GateAvailability updateGateAvailabilityTime(GateAvailability gateAvailability, Date from, Date to) {
        List<GateFlights> busyGates = gateFlightsDao.getBusyGate(gateAvailability.getId(), from, to);
        if (!busyGates.isEmpty()) {
            throw new BadRequestException("Gate can not be available at given time because there are scheduled flights!");
        }
        List<GateAvailability> gateAvailabilityForDate = getGateAvailabilityForDate(gateAvailability.getGate(), from, to);
        if (gateAvailabilityForDate.size() > 1) {
            throw new BadRequestException("Multiple records for given time, please provide another range!");
        }
        GateAvailability availability = gateAvailabilityForDate.iterator().next();
        availability.setAvailableFrom(from);
        availability.setAvailableTo(to);
        return availability;
    }

    @Transactional
    public GateAvailability addGateAvailability(Gate gate, GateAvailabilityModel gateAvailabilityModel) {
        List<GateFlights> busyGates = gateFlightsDao.getBusyGate(gateAvailabilityModel.getGate().getId(),
                gateAvailabilityModel.getAvailableFrom(), gateAvailabilityModel.getAvailableTo());
        if (!busyGates.isEmpty()) {
            throw new BadRequestException("Gate can not be available at given time because there are scheduled flights!");
        }
        List<GateAvailability> byGateIdAndDateBetween = gateAvailabilityDao.getByGateIdAndDateBetween(gate.getId(),
                gateAvailabilityModel.getAvailableFrom(), gateAvailabilityModel.getAvailableTo());
        if (!byGateIdAndDateBetween.isEmpty()) {
            throw new BadRequestException("Gate is already available at given times!");
        }
        GateAvailability gateAvailability = new GateAvailability();
        gateAvailability.setAvailableFrom(gateAvailabilityModel.getAvailableFrom());
        gateAvailability.setAvailableTo(gateAvailabilityModel.getAvailableTo());
        gateAvailability.setGate(gate);
        return gateAvailabilityDao.save(gateAvailability);
    }
}
