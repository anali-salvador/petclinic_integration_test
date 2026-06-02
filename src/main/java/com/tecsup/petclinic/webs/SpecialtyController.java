package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping(value = "/specialties")
    public ResponseEntity<List<SpecialtyDTO>> findAllSpecialties() {
        List<SpecialtyDTO> specialties = specialtyService.findAll();
        return ResponseEntity.ok(specialties);
    }

    @GetMapping(value = "/specialties/{id}")
    ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {
        try {
            SpecialtyDTO specialtyDTO = specialtyService.findById(id);
            return ResponseEntity.ok(specialtyDTO);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/specialties")
    ResponseEntity<SpecialtyDTO> create(@RequestBody SpecialtyDTO specialtyDTO) {
        SpecialtyDTO newSpecialty = specialtyService.create(specialtyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpecialty);
    }

    @PutMapping(value = "/specialties/{id}")
    ResponseEntity<SpecialtyDTO> update(@RequestBody SpecialtyDTO specialtyDTO, @PathVariable Integer id) {
        try {
            SpecialtyDTO existing = specialtyService.findById(id);
            existing.setName(specialtyDTO.getName());
            SpecialtyDTO updated = specialtyService.update(existing);
            return ResponseEntity.ok(updated);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/specialties/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            specialtyService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}