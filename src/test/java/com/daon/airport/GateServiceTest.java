package com.daon.airport;

import com.daon.airport.dao.GateDao;
import com.daon.airport.dao.GateFlightsDao;
import com.daon.airport.entity.Gate;
import com.daon.airport.entity.GateAvailability;
import com.daon.airport.exception.NotFoundException;
import com.daon.airport.service.GateAvailabilityService;
import com.daon.airport.service.GateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GateServiceTest {

    @Mock
    private GateDao gateDao;
    @Mock
    private GateFlightsDao gateFlightsDao;
    @Mock
    private GateAvailabilityService gateAvailabilityService;

    @InjectMocks
    private GateService gateService;

    @Test
    public void getGateByFlightIdTest() {
        Gate gate = prepareGate();
        when(gateFlightsDao.getGateForFlight(anyLong())).thenReturn(gate);
        Gate gateByFlightId = gateService.getGateByFlightId(1L);
        assertEquals(gate, gateByFlightId);
    }

    @Test(expected = NotFoundException.class)
    public void getGateByFlightIdTestNotFound() {
        when(gateFlightsDao.getGateForFlight(anyLong())).thenReturn(null);
        gateService.getGateByFlightId(1L);
    }

    @Test
    public void getGateByIdTest() {
        Gate gate = prepareGate();
        when(gateDao.findById(anyLong())).thenReturn(Optional.of(gate));
        Gate gateByFlightId = gateService.getGateById(1L);
        assertEquals(gate, gateByFlightId);
    }

    @Test(expected = NotFoundException.class)
    public void getGateByIdTestNotFound() {
        when(gateDao.findById(anyLong())).thenReturn(Optional.empty());
        gateService.getGateById(1L);
    }

    @Test(expected = NotFoundException.class)
    public void isGateAvailableTestNotFound() {
        when(gateDao.findById(anyLong())).thenReturn(Optional.empty());
        gateService.isGateAvailable(1L, new Date(), new Date());
    }

    @Test
    public void isGateAvailableTestExpectedTrue() {
        Gate gate = prepareGate();
        when(gateDao.findById(anyLong())).thenReturn(Optional.of(gate));
        when(gateAvailabilityService.getGateAvailabilityForDate(any(Gate.class), any(Date.class), any(Date.class)))
                .thenReturn(Collections.singletonList(new GateAvailability()));
        boolean isGateAvailable = gateService.isGateAvailable(1L, new Date(), new Date());
        assertTrue(isGateAvailable);
    }

    @Test
    public void isGateAvailableTestExpectedFalse() {
        Gate gate = prepareGate();
        when(gateDao.findById(anyLong())).thenReturn(Optional.of(gate));
        when(gateAvailabilityService.getGateAvailabilityForDate(any(Gate.class), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(gateFlightsDao.isGateAvailable(anyLong(), any(Date.class), any(Date.class))).thenReturn(false);
        boolean isGateAvailable = gateService.isGateAvailable(1L, new Date(), new Date());
        assertFalse(isGateAvailable);
    }

    private Gate prepareGate() {
        return new Gate(1L, "Gate1");
    }

}
