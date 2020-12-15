package com.daon.airport.controller;

import com.daon.airport.entity.Gate;
import com.daon.airport.entity.GateAvailability;
import com.daon.airport.model.GateAvailabilityModel;
import com.daon.airport.service.GateAvailabilityService;
import com.daon.airport.service.GateService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/gate/availability_time")
public class GateAvailabilityController {

    private final GateService gateService;
    private final GateAvailabilityService gateAvailabilityService;

    public GateAvailabilityController(GateService gateService, GateAvailabilityService gateAvailabilityService) {
        this.gateService = gateService;
        this.gateAvailabilityService = gateAvailabilityService;
    }

    @PutMapping(value = "/{gateId}/available" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Boolean> markGateAsAvailable(@PathVariable("gateId") Long gateId,
                                                    @RequestBody GateAvailabilityModel gateAvailabilityModel) {
        Gate gate = gateService.getGateById(gateId);
        gateAvailabilityService.markGateAsAvailable(gate, gateAvailabilityModel.getAvailableFrom(),
                gateAvailabilityModel.getAvailableTo());
        return Collections.singletonMap("available", true);
    }

    @PutMapping(value = "/{gateId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public GateAvailabilityModel updateGateAvailabilityTime(@PathVariable("gateId") Long gateId,
                                                            @RequestBody GateAvailabilityModel gateAvailabilityModel) {
        Gate gate = gateService.getGateById(gateId);
        GateAvailability gateAvailability =
                gateAvailabilityService.updateGateAvailabilityTime(gate, gateAvailabilityModel.getAvailableFrom(),
                        gateAvailabilityModel.getAvailableTo());
        return new GateAvailabilityModel(gateAvailability);
    }

    @DeleteMapping(value = "/{gateAvailabilityId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Boolean> removeGateAvailabilityTime(@PathVariable("gateAvailabilityId") Long gateAvailabilityId) {
        gateAvailabilityService.removeGateAvailability(gateAvailabilityId);
        return Collections.singletonMap("removed", true);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GateAvailabilityModel addGateAvailability(@RequestBody GateAvailabilityModel gateAvailabilityModel) {
        Gate gate = gateService.getGateById(gateAvailabilityModel.getGate().getId());
        GateAvailability gateAvailability = gateAvailabilityService.addGateAvailability(gate, gateAvailabilityModel);
        return new GateAvailabilityModel(gateAvailability);
    }

}
