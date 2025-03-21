package br.com.luis.vex.controller;


import br.com.luis.vex.dto.module.ModuleRequestDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/module")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService service;

    @PostMapping
    public ResponseEntity<ModuleResponseDTO> create(@RequestBody ModuleRequestDTO module) {
        return new ResponseEntity<>(service.create(module), HttpStatus.CREATED);
    }
}
