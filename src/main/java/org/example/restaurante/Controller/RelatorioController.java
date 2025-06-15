package org.example.restaurante.Controller;

import org.example.restaurante.service.ComandaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST dedicado à geração de relatórios de vendas.
 */
@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

    private final ComandaService comandaService;

    public RelatorioController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    /**
     * Gera e retorna um relatório de vendas diário.
     * A lógica de negócio para a criação do relatório é delegada ao ComandaService.
     * @param data A data para a qual o relatório deve ser gerado (formato AAAA-MM-DD). Se não for fornecida, usa a data atual.
     * @return Um ResponseEntity contendo um mapa com os dados consolidados do relatório (200 OK).
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> gerarRelatorio(
            @RequestParam(required = false) String data) {

        Map<String, Object> relatorioData = comandaService.gerarRelatorioDiario(data);
        return ResponseEntity.ok(relatorioData);
    }
}