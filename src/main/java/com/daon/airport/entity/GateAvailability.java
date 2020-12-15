package com.daon.airport.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "gate_availability")
public class GateAvailability implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "availalbe_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableFrom;

    @Column(name = "available_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableTo;

    @ManyToOne
    @JoinColumn(name = "gate_id")
    private Gate gate;

    public GateAvailability() {
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

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GateAvailability that = (GateAvailability) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GateAvailability{" +
                "id=" + id +
                ", availableFrom=" + availableFrom +
                ", availableTo=" + availableTo +
                ", gate=" + gate +
                '}';
    }

}
