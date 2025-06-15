
package org.example.restaurante.Controller;

import org.example.restaurante.dto.ItemCardapioDTO; // Importe o DTO
import org.example.restaurante.Model.ItemCardapio;
import org.example.restaurante.service.CardapioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cardapio")
public class CardapioController {

    private final CardapioService cardapioService;

    public CardapioController(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    @GetMapping
    public ResponseEntity<List<ItemCardapioDTO>> listarCardapio() {
        List<ItemCardapioDTO> itens = cardapioService.buscarTodosItensDTO();
        return ResponseEntity.ok(itens);
    }

    @PostMapping
    public ResponseEntity<?> adicionarItem(@RequestBody ItemCardapio item, @RequestParam String chave) {
        try {
            ItemCardapioDTO novoItem = cardapioService.adicionarItem(item, chave);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarItem(@PathVariable Long id) {
        cardapioService.deletarItem(id);
        return ResponseEntity.noContent().build();
    }

}