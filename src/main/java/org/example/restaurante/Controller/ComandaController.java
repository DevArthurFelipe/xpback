package org.example.restaurante.Controller;

import org.example.restaurante.dto.ComandaDTO;
import org.example.restaurante.dto.ItemDTO;
import org.example.restaurante.Model.Comanda;
import org.example.restaurante.Model.ItemCardapio;
import org.example.restaurante.service.CardapioService;
import org.example.restaurante.service.ComandaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciar as operações relacionadas às Comandas.
 * Responsável por receber as requisições HTTP para criar, listar, fechar
 * e modificar as comandas do restaurante.
 */
@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    private final ComandaService comandaService;
    private final CardapioService cardapioService;

    public ComandaController(ComandaService comandaService, CardapioService cardapioService) {
        this.comandaService = comandaService;
        this.cardapioService = cardapioService;
    }

    /**
     * Busca e retorna uma lista de todas as comandas com o status "ABERTA".
     * @return um ResponseEntity contendo a lista de ComandaDTO (200 OK).
     */
    @GetMapping
    public ResponseEntity<List<ComandaDTO>> listarComandasAbertas() {
        List<ComandaDTO> abertas = comandaService.buscarComandasAbertasDTO();
        return ResponseEntity.ok(abertas);
    }

    /**
     * Cria e salva uma nova comanda no sistema.
     * @param comanda O corpo da requisição contendo os dados da nova comanda.
     * @return Um ResponseEntity com o ComandaDTO da nova comanda em caso de sucesso (201 CREATED).
     */
    @PostMapping
    public ResponseEntity<ComandaDTO> salvarNovaComanda(@RequestBody Comanda comanda) {
        ComandaDTO savedComanda = comandaService.salvarNovaComanda(comanda);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComanda);
    }

    /**
     * Altera o status de uma comanda existente para "FECHADA".
     * @param id O ID da comanda a ser fechada, fornecido como uma variável de caminho.
     * @return Um ResponseEntity com o ComandaDTO da comanda atualizada (200 OK).
     */
    @PutMapping("/fechar/{id}")
    public ResponseEntity<ComandaDTO> fecharComanda(@PathVariable Long id) {
        ComandaDTO comandaFechada = comandaService.fecharComanda(id);
        return ResponseEntity.ok(comandaFechada);
    }

    /**
     * Retorna a lista completa de itens disponíveis no cardápio.
     * @return um ResponseEntity contendo a lista de ItemCardapio (200 OK).
     */
    @GetMapping("/cardapio-disponivel")
    public ResponseEntity<List<ItemCardapio>> getCardapioDisponivel() {
        List<ItemCardapio> cardapio = cardapioService.buscarTodosItens();
        return ResponseEntity.ok(cardapio);
    }

    /**
     * Adiciona um novo item a uma comanda já existente.
     * @param comandaId O ID da comanda a ser modificada.
     * @param itemAdicionar O corpo da requisição contendo os dados do item a ser adicionado.
     * @return Um ResponseEntity com o ComandaDTO atualizado em caso de sucesso (200 OK).
     */
    @PostMapping("/{comandaId}/add-item")
    public ResponseEntity<ComandaDTO> adicionarItemAComanda(@PathVariable Long comandaId,
                                                            @RequestBody ItemDTO itemAdicionar) {
        ComandaDTO comandaAtualizada = comandaService.adicionarItemAComandaExistente(
                comandaId, itemAdicionar.getNome(), itemAdicionar.getQuantidade());
        return ResponseEntity.ok(comandaAtualizada);
    }
}