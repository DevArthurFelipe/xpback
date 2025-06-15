package org.example.restaurante.service;

import org.example.restaurante.dto.ComandaDTO; // Importe o DTO
import org.example.restaurante.dto.ItemDTO; // Importe o DTO
import org.example.restaurante.Model.Comanda;
import org.example.restaurante.Model.ItemCardapio;
import org.example.restaurante.Model.ItemComanda;
import org.example.restaurante.repository.ComandaRepository;
import org.example.restaurante.repository.ItemCardapioRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComandaService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ComandaRepository comandaRepository;
    private final ItemCardapioRepository itemCardapioRepository;

    public ComandaService(SimpMessagingTemplate messagingTemplate, ComandaRepository comandaRepository, ItemCardapioRepository itemCardapioRepository) {
        this.messagingTemplate = messagingTemplate;
        this.comandaRepository = comandaRepository;
        this.itemCardapioRepository = itemCardapioRepository;
    }

    public List<ComandaDTO> buscarComandasAbertasDTO() {
        return comandaRepository.findByStatus("ABERTA").stream()
                .map(this::toComandaDTO)
                .collect(Collectors.toList());
    }

    public Comanda inicializarNovaComanda() {
        Comanda comanda = new Comanda();
        comanda.setItens(new ArrayList<>());
        return comanda;
    }

    public ComandaDTO adicionarItemAComandaExistente(Long comandaId, String nomeItem, int quantidade) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada com ID: " + comandaId));

        ItemCardapio itemCardapio = itemCardapioRepository.findByNome(nomeItem)
                .orElseThrow(() -> new RuntimeException("Item não encontrado no cardápio: " + nomeItem));

        ItemComanda itemComanda = new ItemComanda();
        itemComanda.setNome(itemCardapio.getNome());
        itemComanda.setQuantidade(quantidade);
        itemComanda.setPreco(itemCardapio.getPreco());
        itemComanda.setComanda(comanda);

        if (comanda.getItens() == null) {
            comanda.setItens(new ArrayList<>());
        }
        comanda.getItens().add(itemComanda);

        Comanda updatedComanda = comandaRepository.save(comanda);
        messagingTemplate.convertAndSend("/topic/comandas", toComandaDTO(updatedComanda));
        return toComandaDTO(updatedComanda);
    }


    @Transactional
    public ComandaDTO salvarNovaComanda(Comanda comanda) throws IllegalArgumentException {
        if (comanda.getItens() == null || comanda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Adicione ao menos um item à comanda antes de finalizar.");
        }

        comanda.setStatus("ABERTA");
        comanda.setData(LocalDateTime.now());

        for (ItemComanda item : comanda.getItens()) {
            item.setComanda(comanda);
        }

        Comanda savedComanda = comandaRepository.save(comanda);
        messagingTemplate.convertAndSend("/topic/comandas", toComandaDTO(savedComanda));
        return toComandaDTO(savedComanda);
    }

    public ComandaDTO fecharComanda(Long id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada com ID: " + id));

        comanda.setStatus("FECHADA");
        comanda.setDataFechamento(LocalDateTime.now());
        Comanda updatedComanda = comandaRepository.save(comanda);

        messagingTemplate.convertAndSend("/topic/comandas", toComandaDTO(updatedComanda));
        return toComandaDTO(updatedComanda);
    }

    public List<ComandaDTO> buscarComandasFechadasPorDataDTO(LocalDate data) {
        return comandaRepository.findByStatus("FECHADA").stream()
                .filter(c -> c.getDataFechamento() != null && c.getDataFechamento().toLocalDate().equals(data))
                .map(this::toComandaDTO)
                .collect(Collectors.toList());
    }

    public double calcularTotalVendas(List<ComandaDTO> comandas) {
        return comandas.stream()
                .flatMap(c -> c.getItens().stream())
                .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                .sum();
    }

    public Map<Long, Double> calcularTotaisPorComanda(List<ComandaDTO> comandas) {
        Map<Long, Double> totais = new java.util.HashMap<>();
        for (ComandaDTO c : comandas) {
            double total = c.getItens().stream()
                    .mapToDouble(i -> i.getPreco() * i.getQuantidade())
                    .sum();
            totais.put(c.getId(), total);
        }
        return totais;
    }

    public ComandaDTO toComandaDTO(Comanda comanda) {
        List<ItemDTO> itensDTO = comanda.getItens().stream().map(item ->
                new ItemDTO(item.getNome(), item.getQuantidade(), item.getPreco())
        ).collect(Collectors.toList());

        return new ComandaDTO(
                comanda.getId(),
                comanda.getMesa(),
                comanda.getTipoConsumo(),
                comanda.getStatus(),
                comanda.getObservacao(),
                itensDTO
        );
    }
}