package org.example.restaurante.dto;

import lombok.AllArgsConstructor; // Adicionado
import lombok.Data; // Adicionado
import lombok.NoArgsConstructor; // Adicionado

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private String nome;
    private int quantidade;
    private double preco;

}