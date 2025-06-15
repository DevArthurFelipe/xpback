package org.example.restaurante.Model;

import jakarta.persistence.*;
import lombok.Data; // Adicionado
import lombok.NoArgsConstructor; // Adicionado
import lombok.AllArgsConstructor; // Adicionado
import lombok.EqualsAndHashCode; // Adicionado para lidar com a referência cíclica na Comanda
import lombok.ToString; // Adicionado para lidar com a referência cíclica na Comanda

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "comanda")
@ToString(exclude = "comanda")
public class ItemComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private double preco;
    private int quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comanda_id")
    private Comanda comanda;


}