package com.daon.airport.controller;

import com.daon.airport.entity.Gate;
import com.daon.airport.model.GateAvailabilityModel;
import com.daon.airport.model.GateModel;
import com.daon.airport.service.GateService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/gate")
public class GateController {

    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping(value = "/flight/{flightId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public GateModel getGateByFlight(@PathVariable("flightId") Long flightId) {
        return new GateModel(gateService.getGateByFlightId(flightId));
    }

    @PutMapping(value = "/availability/{gateId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Boolean> isGateAvailable(@PathVariable("gateId") Long gateId,
                                                @RequestBody GateAvailabilityModel gateAvailabilityModel) {
        boolean isGateAvailable =
                gateService.isGateAvailable(gateId, gateAvailabilityModel.getAvailableFrom(), gateAvailabilityModel.getAvailableTo());
        return Collections.singletonMap("available", isGateAvailable);
    }

}
