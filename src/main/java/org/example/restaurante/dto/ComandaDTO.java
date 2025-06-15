package org.example.restaurante.dto;

import lombok.AllArgsConstructor; // Adicionado
import lombok.Data; // Adicionado
import lombok.NoArgsConstructor; // Adicionado

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComandaDTO {
    private Long id;
    private String mesa;
    private String tipoConsumo;
    private String status;
    private String observacao;
    private List<ItemDTO> itens;

}