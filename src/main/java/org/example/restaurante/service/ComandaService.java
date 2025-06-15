package org.example.restaurante.service;

import org.example.restaurante.dto.ComandaDTO;
import org.example.restaurante.dto.ItemDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Camada de serviço contendo a lógica de negócio para as Comandas.
 * Orquestra as operações de acesso a dados através dos repositórios
 * e realiza os cálculos e manipulações necessárias para o gerenciamento das comandas.
 */
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

    /**
     * Busca no banco de dados todas as comandas que estão com o status "ABERTA".
     * @return Uma lista de ComandaDTO representando as comandas abertas.
     */
    public List<ComandaDTO> buscarComandasAbertasDTO() {
        return comandaRepository.findByStatus("ABERTA").stream()
                .map(this::toComandaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cria uma nova instância de Comanda em memória, pronta para receber itens.
     * @return um objeto Comanda novo e vazio.
     */
    public Comanda inicializarNovaComanda() {
        Comanda comanda = new Comanda();
        comanda.setItens(new ArrayList<>());
        return comanda;
    }

    /**
     * Adiciona um item a uma comanda já existente.
     * @param comandaId O ID da comanda a ser modificada.
     * @param nomeItem O nome do item do cardápio a ser adicionado.
     * @param quantidade A quantidade do item.
     * @return O DTO da comanda atualizada.
     * @throws RuntimeException se a comanda ou o item do cardápio não forem encontrados.
     */
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

    /**
     * Salva uma nova comanda no banco de dados.
     * Define o status como "ABERTA" e a data de criação.
     * @param comanda A entidade Comanda a ser salva.
     * @return O DTO da comanda salva.
     * @throws IllegalArgumentException se a comanda não tiver itens.
     */
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

    /**
     * Altera o status de uma comanda para "FECHADA" e registra a data/hora do fechamento.
     * Notifica os clientes via WebSocket sobre a atualização da comanda.
     * @param id O ID da comanda a ser fechada.
     * @return O DTO da comanda atualizada com o novo status.
     * @throws RuntimeException se nenhuma comanda for encontrada com o ID fornecido.
     */
    public ComandaDTO fecharComanda(Long id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comanda não encontrada com ID: " + id));

        comanda.setStatus("FECHADA");
        comanda.setDataFechamento(LocalDateTime.now());
        Comanda updatedComanda = comandaRepository.save(comanda);

        messagingTemplate.convertAndSend("/topic/comandas", toComandaDTO(updatedComanda));
        return toComandaDTO(updatedComanda);
    }

    /**
     * Busca todas as comandas fechadas em uma data específica.
     * @param data A data para a qual as comandas fechadas serão buscadas.
     * @return Uma lista de ComandaDTO representando as comandas fechadas.
     */
    public List<ComandaDTO> buscarComandasFechadasPorDataDTO(LocalDate data) {
        return comandaRepository.findByStatus("FECHADA").stream()
                .filter(c -> c.getDataFechamento() != null && c.getDataFechamento().toLocalDate().equals(data))
                .map(this::toComandaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calcula o valor total de vendas a partir de uma lista de comandas.
     * @param comandas A lista de ComandaDTO para o cálculo.
     * @return O valor total somado de todos os itens de todas as comandas.
     */
    public double calcularTotalVendas(List<ComandaDTO> comandas) {
        return comandas.stream()
                .flatMap(c -> c.getItens().stream())
                .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                .sum();
    }

    /**
     * Calcula o valor total para cada comanda individualmente em uma lista.
     * @param comandas A lista de ComandaDTO para o cálculo.
     * @return Um mapa onde a chave é o ID da comanda e o valor é o total daquela comanda.
     */
    public Map<Long, Double> calcularTotaisPorComanda(List<ComandaDTO> comandas) {
        Map<Long, Double> totais = new HashMap<>();
        for (ComandaDTO c : comandas) {
            double total = c.getItens().stream()
                    .mapToDouble(i -> i.getPreco() * i.getQuantidade())
                    .sum();
            totais.put(c.getId(), total);
        }
        return totais;
    }

    /**
     * Converte uma entidade Comanda para seu respectivo DTO.
     * @param comanda A entidade a ser convertida.
     * @return O objeto ComandaDTO.
     */
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

    /**
     * Orquestra a criação de um relatório diário de vendas.
     * @param dataStr A data do relatório no formato AAAA-MM-DD. Se nulo ou vazio, usa a data atual.
     * @return Um mapa contendo os dados consolidados do relatório.
     */
    public Map<String, Object> gerarRelatorioDiario(String dataStr) {
        LocalDate dataRelatorio = (dataStr != null && !dataStr.isEmpty()) ? LocalDate.parse(dataStr) : LocalDate.now();

        List<ComandaDTO> comandasFechadasHoje = this.buscarComandasFechadasPorDataDTO(dataRelatorio);
        double totalVendas = this.calcularTotalVendas(comandasFechadasHoje);
        Map<Long, Double> totaisPorComanda = this.calcularTotaisPorComanda(comandasFechadasHoje);

        Map<String, Object> relatorioData = new HashMap<>();
        relatorioData.put("data", dataRelatorio);
        relatorioData.put("comandas", comandasFechadasHoje);
        relatorioData.put("totaisPorComanda", totaisPorComanda);
        relatorioData.put("totalVendas", totalVendas);

        return relatorioData;
    }
}