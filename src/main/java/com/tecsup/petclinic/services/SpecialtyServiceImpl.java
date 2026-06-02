package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public List<SpecialtyDTO> findAll() {
        List<Specialty> specialties = specialtyRepository.findAll();
        return specialties.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SpecialtyDTO findById(Integer id) throws SpecialtyNotFoundException {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new SpecialtyNotFoundException("Specialty not found with id: " + id));
        return toDTO(specialty);
    }

    @Override
    public SpecialtyDTO create(SpecialtyDTO specialtyDTO) {
        Specialty specialty = toEntity(specialtyDTO);
        Specialty saved = specialtyRepository.save(specialty);
        return toDTO(saved);
    }

    @Override
    public SpecialtyDTO update(SpecialtyDTO specialtyDTO) throws SpecialtyNotFoundException {
        Specialty specialty = specialtyRepository.findById(specialtyDTO.getId())
                .orElseThrow(() -> new SpecialtyNotFoundException("Specialty not found with id: " + specialtyDTO.getId()));
        specialty.setName(specialtyDTO.getName());
        Specialty updated = specialtyRepository.save(specialty);
        return toDTO(updated);
    }

    @Override
    public void delete(Integer id) throws SpecialtyNotFoundException {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new SpecialtyNotFoundException("Specialty not found with id: " + id));
        specialtyRepository.delete(specialty);
    }

    private SpecialtyDTO toDTO(Specialty specialty) {
        return SpecialtyDTO.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .build();
    }

    private Specialty toEntity(SpecialtyDTO specialtyDTO) {
        Specialty specialty = new Specialty();
        specialty.setName(specialtyDTO.getName());
        return specialty;
    }
}