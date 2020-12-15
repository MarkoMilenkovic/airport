package com.daon.airport.model;

import com.daon.airport.entity.Gate;

import java.util.Objects;

public class GateModel {

    private Long id;
    private String name;

    public GateModel() {
    }

    public GateModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GateModel(Gate gate) {
        this.id = gate.getId();
        this.name = gate.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GateModel gateModel = (GateModel) o;
        return id.equals(gateModel.id) &&
                name.equals(gateModel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
