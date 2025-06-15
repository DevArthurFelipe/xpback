package org.example.restaurante.repository;

import org.example.restaurante.Model.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    List<Comanda> findByStatus(String status);
}
