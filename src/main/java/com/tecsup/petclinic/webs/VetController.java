package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VetController {

    private final VetService vetService;

    public VetController(VetService vetService) {
        this.vetService = vetService;
    }

    @GetMapping(value = "/vets")
    public ResponseEntity<List<VetDTO>> findAllVets() {
        List<VetDTO> vets = vetService.findAll();
        return ResponseEntity.ok(vets);
    }

    @GetMapping(value = "/vets/{id}")
    ResponseEntity<VetDTO> findById(@PathVariable Integer id) {
        try {
            VetDTO vetDTO = vetService.findById(id);
            return ResponseEntity.ok(vetDTO);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/vets")
    ResponseEntity<VetDTO> create(@RequestBody VetDTO vetDTO) {
        VetDTO newVet = vetService.create(vetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVet);
    }

    @PutMapping(value = "/vets/{id}")
    ResponseEntity<VetDTO> update(@RequestBody VetDTO vetDTO, @PathVariable Integer id) {
        try {
            VetDTO existing = vetService.findById(id);
            existing.setFirstName(vetDTO.getFirstName());
            existing.setLastName(vetDTO.getLastName());
            VetDTO updated = vetService.update(existing);
            return ResponseEntity.ok(updated);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/vets/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            vetService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}