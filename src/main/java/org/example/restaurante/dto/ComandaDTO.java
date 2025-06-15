package org.example.restaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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