
package org.example.restaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCardapioDTO {
    private Long id;
    private String nome;
    private double preco;
}