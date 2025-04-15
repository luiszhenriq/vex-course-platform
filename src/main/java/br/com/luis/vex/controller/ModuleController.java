package br.com.luis.vex.controller;


import br.com.luis.vex.dto.module.ModuleRequestDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.dto.module.ModuleUpdateDTO;
import br.com.luis.vex.service.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/module")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService service;


    @Operation(summary = "Create module")
    @ApiResponses(value = {
            @ApiResponse(responseCode="201", description="Module created successfully"),
            @ApiResponse(responseCode="400", description="Invalid input data"),
            @ApiResponse(responseCode="404", description="Course not found"),
            @ApiResponse(responseCode="401", description="Unauthorized"),
    })
    @PostMapping
    public ResponseEntity<ModuleResponseDTO> create(@RequestBody @Valid ModuleRequestDTO module) {
        return new ResponseEntity<>(service.create(module), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete module by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode="204", description="Module deleted successfully"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get module by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Module listed successfully"),
            @ApiResponse(responseCode="404", description="Module not found"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ModuleResponseDTO> findById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Update module")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Module updated successfully"),
            @ApiResponse(responseCode="400", description="Invalid input data"),
            @ApiResponse(responseCode="404", description="Module not found"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ModuleResponseDTO> update(@PathVariable("id") UUID id,
                                                    @RequestBody @Valid ModuleUpdateDTO moduleUpdate) {
        return new ResponseEntity<>(service.update(id, moduleUpdate), HttpStatus.OK);
    }
}
