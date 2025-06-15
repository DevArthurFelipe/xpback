package org.example.restaurante.Controller;

import org.example.restaurante.service.ComandaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

        Map<String, Object> relatorioData = comandaService.gerarRelatorioDiario(data);
        return ResponseEntity.ok(relatorioData);
    }
}