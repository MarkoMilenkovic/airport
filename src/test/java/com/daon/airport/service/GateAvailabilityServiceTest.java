package com.daon.airport.service;

import com.daon.airport.dao.GateAvailabilityDao;
import com.daon.airport.dao.GateFlightsDao;
import com.daon.airport.entity.Gate;
import com.daon.airport.entity.GateAvailability;
import com.daon.airport.entity.GateFlights;
import com.daon.airport.exception.BadRequestException;
import com.daon.airport.model.GateAvailabilityModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GateAvailabilityServiceTest {

    @Mock
    private GateAvailabilityDao gateAvailabilityDao;
    @Mock
    private GateFlightsDao gateFlightsDao;

    @InjectMocks
    private GateAvailabilityService gateAvailabilityService;

    @Test
    public void getGateAvailabilityForDateTest() {
        GateAvailability gateAvailability = prepareGateAvailability();
        when(gateAvailabilityDao.getByGateIdAndDateBetween(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.singletonList(gateAvailability));
        List<GateAvailability> availability =
                gateAvailabilityService.getGateAvailabilityForDate(gateAvailability.getGate(), new Date(), new Date());
        assertNotNull(availability);
        assertEquals(1, availability.size());
        assertEquals(gateAvailability, availability.iterator().next());
    }

    @Test(expected = BadRequestException.class)
    public void markGateAsAvailableTestGateAlreadyAvailable() {
        when(gateFlightsDao.getBusyGate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        gateAvailabilityService.markGateAsAvailable(prepareGate(), new Date(), new Date());
    }

    @Test
    public void markGateAsAvailableTest() {
        GateFlights gateFlights = prepareGateFlights();
        when(gateFlightsDao.getBusyGate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.singletonList(gateFlights));
        gateAvailabilityService.markGateAsAvailable(prepareGate(), new Date(), new Date());
        assertTrue(gateFlights.isTerminated());
    }

    @Test(expected = BadRequestException.class)
    public void updateGateAvailabilityTimeThereAreScheduledFlights() {
        GateFlights gateFlights = prepareGateFlights();
        when(gateFlightsDao.getBusyGate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.singletonList(gateFlights));
        gateAvailabilityService.updateGateAvailabilityTime(prepareGate(), new Date(), new Date());
        verifyNoInteractions(gateAvailabilityDao);
    }

    @Test(expected = BadRequestException.class)
    public void updateGateAvailabilityTimeInvalidRange() {
        when(gateFlightsDao.getBusyGate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(gateAvailabilityDao.getByGateIdAndDateBetween(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(prepareGateAvailability(), prepareGateAvailability()));
        gateAvailabilityService.updateGateAvailabilityTime(prepareGate(), new Date(), new Date());
        verify(gateAvailabilityDao, times(1))
                .getByGateIdAndDateBetween(anyLong(), any(Date.class), any(Date.class));
    }

    @Test
    public void updateGateAvailabilityTimeSuccess() {
        GateAvailability gateAvailability = prepareGateAvailability();
        when(gateFlightsDao.getBusyGate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(gateAvailabilityDao.getByGateIdAndDateBetween(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.singletonList(gateAvailability));
        Date dateFrom = new Date();
        Date dateTo = new Date();
        gateAvailabilityService.updateGateAvailabilityTime(prepareGate(), dateFrom, dateTo);
        assertEquals(dateFrom, gateAvailability.getAvailableFrom());
        assertEquals(dateTo, gateAvailability.getAvailableTo());
    }


    @Test(expected = BadRequestException.class)
    public void addGateAvailabilityTestDatesAreIncorrect() {
        Gate gate = prepareGate();
        when(gateFlightsDao.getBusyGate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(gateAvailabilityDao.getByGateIdAndDateBetween(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        GateAvailabilityModel model = new GateAvailabilityModel();
        model.setAvailableFrom(new Date());
        model.setAvailableTo(Date.from(Instant.now().minus(Duration.ofDays(1))));

        gateAvailabilityService.addGateAvailability(gate, model);

        verify(gateFlightsDao, times(1))
                .getBusyGate(gate.getId(), model.getAvailableFrom(), model.getAvailableTo());
        verify(gateAvailabilityDao, times(1))
                .getByGateIdAndDateBetween(gate.getId(), model.getAvailableFrom(), model.getAvailableTo());
    }

    @Test
    public void addGateAvailabilitySuccess() {
        Gate gate = prepareGate();
        when(gateFlightsDao.getBusyGate(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(gateAvailabilityDao.getByGateIdAndDateBetween(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(gateAvailabilityDao.save(any(GateAvailability.class))).thenAnswer(argument -> {
            GateAvailability ga = (GateAvailability) argument.getArguments()[0];
            ga.setId(1L);
            return ga;
        });
        GateAvailabilityModel model = new GateAvailabilityModel();
        model.setAvailableTo(new Date());
        model.setAvailableFrom(Date.from(Instant.now().minus(Duration.ofDays(1))));

        GateAvailability gateAvailability = gateAvailabilityService.addGateAvailability(gate, model);

        verify(gateFlightsDao, times(1))
                .getBusyGate(gate.getId(), model.getAvailableFrom(), model.getAvailableTo());
        verify(gateAvailabilityDao, times(1))
                .getByGateIdAndDateBetween(gate.getId(), model.getAvailableFrom(), model.getAvailableTo());
        verify(gateAvailabilityDao, times(1)).save(any());

        assertEquals(Long.valueOf(1L), gateAvailability.getId());
    }

    private GateAvailability prepareGateAvailability() {
        GateAvailability gateAvailability = new GateAvailability();
        gateAvailability.setId(1L);
        gateAvailability.setAvailableFrom(new Date());
        Instant after= Instant.now().plus(Duration.ofDays(1));
        Date dateAfter = Date.from(after);
        gateAvailability.setAvailableTo(dateAfter);
        gateAvailability.setGate(prepareGate());
        return gateAvailability;
    }

    private Gate prepareGate() {
        return new Gate(1L, "Gate1");
    }

    private GateFlights prepareGateFlights() {
        GateFlights gateFlights = new GateFlights();
        gateFlights.setIsTerminated(false);
        gateFlights.setGate(prepareGate());
        return gateFlights;
    }


}
