package org.example.restaurante.Controller;

import org.example.restaurante.dto.ItemCardapioDTO;
import org.example.restaurante.Model.ItemCardapio;
import org.example.restaurante.service.CardapioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para o gerenciamento do cardápio do restaurante.
 * Permite listar, adicionar e remover itens do cardápio.
 */
@RestController
@RequestMapping("/api/cardapio")
public class CardapioController {

    private final CardapioService cardapioService;

    public CardapioController(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    /**
     * Retorna a lista completa de itens disponíveis no cardápio.
     * @return um ResponseEntity contendo a lista de ItemCardapioDTO (200 OK).
     */
    @GetMapping
    public ResponseEntity<List<ItemCardapioDTO>> listarCardapio() {
        List<ItemCardapioDTO> itens = cardapioService.buscarTodosItensDTO();
        return ResponseEntity.ok(itens);
    }

    /**
     * Adiciona um novo item ao cardápio.
     * Requer uma chave de acesso para autorização.
     * @param item O corpo da requisição com os dados do novo item.
     * @param chave A chave mestra para autorizar a operação.
     * @return O novo item criado como ItemCardapioDTO (201 CREATED).
     */
    @PostMapping
    public ResponseEntity<ItemCardapioDTO> adicionarItem(@RequestBody ItemCardapio item, @RequestParam String chave) {
        ItemCardapioDTO novoItem = cardapioService.adicionarItem(item, chave);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

    /**
     * Deleta um item do cardápio com base no seu ID.
     * @param id O ID do item a ser deletado.
     * @return Uma resposta vazia com status (204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItem(@PathVariable Long id) {
        cardapioService.deletarItem(id);
        return ResponseEntity.noContent().build();
    }
}