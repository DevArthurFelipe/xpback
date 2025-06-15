package org.example.restaurante.service;

import org.example.restaurante.dto.ItemCardapioDTO; // Importe o DTO
import org.example.restaurante.Model.ItemCardapio;
import org.example.restaurante.repository.ItemCardapioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardapioService {

    private final ItemCardapioRepository itemCardapioRepository;
    private final String CHAVE_MESTRE = "SEGREDO123";

    public CardapioService(ItemCardapioRepository itemCardapioRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
    }

    public List<ItemCardapioDTO> buscarTodosItensDTO() { // Novo método que retorna DTO
        return itemCardapioRepository.findAll().stream()
                .map(this::toItemCardapioDTO)
                .collect(Collectors.toList());
    }

    public Optional<ItemCardapio> buscarItemPorNome(String nome) {
        return itemCardapioRepository.findByNome(nome);
    }

    public ItemCardapioDTO adicionarItem(ItemCardapio item, String chaveAcesso) throws IllegalArgumentException {
        if (!chaveAcesso.equals(CHAVE_MESTRE)) {
            throw new IllegalArgumentException("Chave de acesso inválida!");
        }
        ItemCardapio savedItem = itemCardapioRepository.save(item);
        return toItemCardapioDTO(savedItem);
    }

    public void deletarItem(Long id) {
        itemCardapioRepository.deleteById(id);
    }

    public boolean validarChaveMestre(String chave) {
        return chave.equals(CHAVE_MESTRE);
    }

    private ItemCardapioDTO toItemCardapioDTO(ItemCardapio item) {
        return new ItemCardapioDTO(item.getId(), item.getNome(), item.getPreco());
    }

    public List<ItemCardapio> buscarTodosItens() {
        return itemCardapioRepository.findAll();
    }
}
