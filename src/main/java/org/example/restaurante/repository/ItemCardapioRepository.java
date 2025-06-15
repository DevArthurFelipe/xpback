package org.example.restaurante.repository;

import org.example.restaurante.Model.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {
    Optional<ItemCardapio> findByNome(String nome);
}
