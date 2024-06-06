package com.gabrielfernandes.bdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gabrielfernandes.bdv.model.Mesa;



@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
}
