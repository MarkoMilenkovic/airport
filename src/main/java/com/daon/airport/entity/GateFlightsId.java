package com.daon.airport.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GateFlightsId implements Serializable {

    @Column(name = "gate_id")
    private Long gateId;

    @Column(name = "fight_id")
    private Long flightId;

    private GateFlightsId() {}

    public GateFlightsId(Long gateId, Long flightId) {
        this.gateId = gateId;
        this.flightId = flightId;
    }

    public Long getGateId() {
        return gateId;
    }

    public void setGateId(Long gateId) {
        this.gateId = gateId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        GateFlightsId that = (GateFlightsId) o;
        return Objects.equals(gateId, that.gateId) &&
               Objects.equals(flightId, that.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gateId, flightId);
    }

    @Override
    public String toString() {
        return "GateFlightsId{" +
                "gateId=" + gateId +
                ", flightId=" + flightId +
                '}';
    }

}