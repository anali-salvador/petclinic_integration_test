package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.repositories.PetRepository;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;

    public VisitServiceImpl(VisitRepository visitRepository, PetRepository petRepository) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
    }

    @Override
    public List<VisitDTO> findAll() {
        List<Visit> visits = visitRepository.findAll();
        return visits.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VisitDTO findById(Long id) throws VisitNotFoundException {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with id: " + id));
        return toDTO(visit);
    }

    @Override
    public VisitDTO create(VisitDTO visitDTO) {
        Visit visit = toEntity(visitDTO);
        Visit saved = visitRepository.save(visit);
        return toDTO(saved);
    }

    @Override
    public VisitDTO update(VisitDTO visitDTO) throws VisitNotFoundException {
        Visit visit = visitRepository.findById(visitDTO.getId())
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with id: " + visitDTO.getId()));
        visit.setVisitDate(LocalDate.parse(visitDTO.getVisitDate()));
        visit.setDescription(visitDTO.getDescription());
        Visit updated = visitRepository.save(visit);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) throws VisitNotFoundException {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with id: " + id));
        visitRepository.delete(visit);
    }

    private VisitDTO toDTO(Visit visit) {
        return VisitDTO.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate() != null ? visit.getVisitDate().toString() : null)
                .description(visit.getDescription())
                .petId(visit.getPet() != null ? visit.getPet().getId() : null)
                .build();
    }

    private Visit toEntity(VisitDTO visitDTO) {
        Visit visit = new Visit();
        visit.setVisitDate(visitDTO.getVisitDate() != null ? LocalDate.parse(visitDTO.getVisitDate()) : null);
        visit.setDescription(visitDTO.getDescription());
        if (visitDTO.getPetId() != null) {
            Pet pet = petRepository.findById(visitDTO.getPetId())
                    .orElse(null);
            visit.setPet(pet);
        }
        return visit;
    }
}