package org.example.restaurante.service;

import org.example.restaurante.dto.ItemCardapioDTO;
import org.example.restaurante.Model.ItemCardapio;
import org.example.restaurante.repository.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Camada de serviço que encapsula a lógica de negócio para o gerenciamento do cardápio.
 */
@Service
public class CardapioService {

    private final ItemCardapioRepository itemCardapioRepository;

    @Value("${app.security.master-key}")
    private String chaveMestre;

    public CardapioService(ItemCardapioRepository itemCardapioRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
    }

    /**
     * Busca todos os itens do cardápio no banco de dados e os converte para DTOs.
     * @return Uma lista de ItemCardapioDTO.
     */
    public List<ItemCardapioDTO> buscarTodosItensDTO() {
        return itemCardapioRepository.findAll().stream()
                .map(this::toItemCardapioDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um item específico do cardápio pelo seu nome.
     * @param nome O nome do item a ser buscado.
     * @return um Optional contendo o ItemCardapio se encontrado.
     */
    public Optional<ItemCardapio> buscarItemPorNome(String nome) {
        return itemCardapioRepository.findByNome(nome);
    }

    /**
     * Salva um novo item no cardápio após validar a chave de acesso.
     * @param item O novo item a ser salvo.
     * @param chaveAcesso A chave mestra para autorização.
     * @return O DTO do item salvo.
     * @throws IllegalArgumentException se a chave de acesso for inválida.
     */
    public ItemCardapioDTO adicionarItem(ItemCardapio item, String chaveAcesso) throws IllegalArgumentException {
        if (!chaveAcesso.equals(this.chaveMestre)) {
            throw new IllegalArgumentException("Chave de acesso inválida!");
        }
        ItemCardapio savedItem = itemCardapioRepository.save(item);
        return toItemCardapioDTO(savedItem);
    }

    /**
     * Deleta um item do cardápio pelo seu ID.
     * @param id O ID do item a ser deletado.
     */
    public void deletarItem(Long id) {
        itemCardapioRepository.deleteById(id);
    }

    /**
     * Valida se a chave fornecida corresponde à chave mestra da aplicação.
     * @param chave A chave a ser validada.
     * @return true se a chave for válida, false caso contrário.
     */
    public boolean validarChaveMestre(String chave) {
        return chave.equals(this.chaveMestre);
    }

    /**
     * Converte uma entidade ItemCardapio para seu respectivo DTO.
     * @param item A entidade a ser convertida.
     * @return O objeto ItemCardapioDTO.
     */
    private ItemCardapioDTO toItemCardapioDTO(ItemCardapio item) {
        return new ItemCardapioDTO(item.getId(), item.getNome(), item.getPreco());
    }

    /**
     * Busca todos os itens do cardápio e os retorna como entidades.
     * @return Uma lista de entidades ItemCardapio.
     */
    public List<ItemCardapio> buscarTodosItens() {
        return itemCardapioRepository.findAll();
    }
}