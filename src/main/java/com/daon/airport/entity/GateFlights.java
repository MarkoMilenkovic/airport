package com.daon.airport.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "gate_flights")
public class GateFlights implements Serializable {

    @EmbeddedId
    private GateFlightsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gateId")
    private Gate gate;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("flightId")
    private Flight flight;

    @Column(name = "arrived_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivedAt;

    @Column(name = "gate_locked_until")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gateLockedUntil;

    @Column(name = "is_terminated")
    private Boolean isTerminated;

    public GateFlights() {
    }

    public GateFlightsId getId() {
        return id;
    }

    public void setId(GateFlightsId id) {
        this.id = id;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Date getArrivedAt() {
        return arrivedAt;
    }

    public void setArrivedAt(Date arrivedAt) {
        this.arrivedAt = arrivedAt;
    }

    public Date getGateLockedUntil() {
        return gateLockedUntil;
    }

    public void setGateLockedUntil(Date gateLockedUntil) {
        this.gateLockedUntil = gateLockedUntil;
    }

    public Boolean isTerminated() {
        return isTerminated;
    }

    public void setIsTerminated(Boolean isTerminated) {
        this.isTerminated = isTerminated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        GateFlights that = (GateFlights) o;
        return Objects.equals(gate, that.gate) &&
                Objects.equals(flight, that.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gate, flight);
    }

}
