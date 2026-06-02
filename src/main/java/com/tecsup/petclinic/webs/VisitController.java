package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping(value = "/visits")
    public ResponseEntity<List<VisitDTO>> findAllVisits() {
        List<VisitDTO> visits = visitService.findAll();
        return ResponseEntity.ok(visits);
    }

    @GetMapping(value = "/visits/{id}")
    ResponseEntity<VisitDTO> findById(@PathVariable Long id) {
        try {
            VisitDTO visitDTO = visitService.findById(id);
            return ResponseEntity.ok(visitDTO);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/visits")
    ResponseEntity<VisitDTO> create(@RequestBody VisitDTO visitDTO) {
        VisitDTO newVisit = visitService.create(visitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVisit);
    }

    @PutMapping(value = "/visits/{id}")
    ResponseEntity<VisitDTO> update(@RequestBody VisitDTO visitDTO, @PathVariable Long id) {
        try {
            VisitDTO existing = visitService.findById(id);
            existing.setVisitDate(visitDTO.getVisitDate());
            existing.setDescription(visitDTO.getDescription());
            VisitDTO updated = visitService.update(existing);
            return ResponseEntity.ok(updated);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/visits/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            visitService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}