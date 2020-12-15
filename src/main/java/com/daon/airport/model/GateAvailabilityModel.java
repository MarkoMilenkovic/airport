package com.daon.airport.model;

import com.daon.airport.entity.GateAvailability;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Objects;

public class GateAvailabilityModel {

    private Long id;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
    private Date availableFrom;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
    private Date availableTo;
    private GateModel gate;

    public GateAvailabilityModel() {
    }

    public GateAvailabilityModel(GateAvailability gateAvailability) {
        this.id = gateAvailability.getId();
        this.availableFrom = gateAvailability.getAvailableFrom();
        this.availableTo = gateAvailability.getAvailableTo();
        this.gate = new GateModel(gateAvailability.getGate());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Date getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(Date availableTo) {
        this.availableTo = availableTo;
    }

    public GateModel getGate() {
        return gate;
    }

    public void setGateId(GateModel gate) {
        this.gate = gate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GateAvailabilityModel that = (GateAvailabilityModel) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GateAvailabilityModel{" +
                "id=" + id +
                ", availableFrom=" + availableFrom +
                ", availableTo=" + availableTo +
                ", gateId=" + gate +
                '}';
    }

}
