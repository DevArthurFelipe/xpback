
package org.example.restaurante.Controller;

import org.example.restaurante.dto.ComandaDTO; // Importe o DTO
import org.example.restaurante.dto.ItemDTO;
import org.example.restaurante.Model.Comanda;
import org.example.restaurante.Model.ItemCardapio;
import org.example.restaurante.service.CardapioService;
import org.example.restaurante.service.ComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    private final ComandaService comandaService;
    private final CardapioService cardapioService;

    public ComandaController(ComandaService comandaService, CardapioService cardapioService) {
        this.comandaService = comandaService;
        this.cardapioService = cardapioService;
    }

    @GetMapping
    public ResponseEntity<List<ComandaDTO>> listarComandasAbertas() {
        List<ComandaDTO> abertas = comandaService.buscarComandasAbertasDTO();
        return ResponseEntity.ok(abertas);
    }

    @PostMapping
    public ResponseEntity<?> salvarNovaComanda(@RequestBody Comanda comanda) {
        try {
            ComandaDTO savedComanda = comandaService.salvarNovaComanda(comanda);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedComanda);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/fechar/{id}")
    public ResponseEntity<ComandaDTO> fecharComanda(@PathVariable Long id) {
        ComandaDTO comandaFechada = comandaService.fecharComanda(id);
        return ResponseEntity.ok(comandaFechada);
    }

    @GetMapping("/cardapio-disponivel")
    public ResponseEntity<List<ItemCardapio>> getCardapioDisponivel() {
        List<ItemCardapio> cardapio = cardapioService.buscarTodosItens();
        return ResponseEntity.ok(cardapio);
    }

    @PostMapping("/{comandaId}/add-item")
    public ResponseEntity<?> adicionarItemAComanda(@PathVariable Long comandaId,
                                                   @RequestBody ItemDTO itemAdicionar) {
        try {
            ComandaDTO comandaAtualizada = comandaService.adicionarItemAComandaExistente(
                    comandaId, itemAdicionar.getNome(), itemAdicionar.getQuantidade());
            return ResponseEntity.ok(comandaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}