package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping(value = "/owners")
    public ResponseEntity<List<OwnerDTO>> findAllOwners() {
        List<OwnerDTO> owners = ownerService.findAll();
        return ResponseEntity.ok(owners);
    }

    @GetMapping(value = "/owners/{id}")
    ResponseEntity<OwnerDTO> findById(@PathVariable Long id) {
        try {
            OwnerDTO ownerDTO = ownerService.findById(id);
            return ResponseEntity.ok(ownerDTO);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/owners")
    ResponseEntity<OwnerDTO> create(@RequestBody OwnerDTO ownerDTO) {
        OwnerDTO newOwner = ownerService.create(ownerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOwner);
    }

    @PutMapping(value = "/owners/{id}")
    ResponseEntity<OwnerDTO> update(@RequestBody OwnerDTO ownerDTO, @PathVariable Long id) {
        try {
            OwnerDTO existing = ownerService.findById(id);
            existing.setFirstName(ownerDTO.getFirstName());
            existing.setLastName(ownerDTO.getLastName());
            existing.setAddress(ownerDTO.getAddress());
            existing.setCity(ownerDTO.getCity());
            existing.setTelephone(ownerDTO.getTelephone());
            OwnerDTO updated = ownerService.update(existing);
            return ResponseEntity.ok(updated);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/owners/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            ownerService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}