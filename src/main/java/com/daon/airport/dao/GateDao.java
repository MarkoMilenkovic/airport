package com.daon.airport.dao;

import com.daon.airport.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateDao extends JpaRepository<Gate, Long> {
}
