
package org.example.restaurante.Controller;

import org.example.restaurante.dto.ComandaDTO; // Importe o DTO
import org.example.restaurante.Model.Comanda;
import org.example.restaurante.service.ComandaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

    private final ComandaService comandaService;

    public RelatorioController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> gerarRelatorio(
            @RequestParam(required = false) String data) {

        LocalDate dataRelatorio = (data != null) ? LocalDate.parse(data) : LocalDate.now();

        List<ComandaDTO> comandasFechadasHoje = comandaService.buscarComandasFechadasPorDataDTO(dataRelatorio);

        double totalVendas = comandaService.calcularTotalVendas(comandasFechadasHoje);
        Map<Long, Double> totaisPorComanda = comandaService.calcularTotaisPorComanda(comandasFechadasHoje);

        Map<String, Object> relatorioData = new HashMap<>();
        relatorioData.put("data", dataRelatorio);
        relatorioData.put("comandas", comandasFechadasHoje);
        relatorioData.put("totaisPorComanda", totaisPorComanda);
        relatorioData.put("totalVendas", totalVendas);

        return ResponseEntity.ok(relatorioData);
    }
}