package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;

    public VetServiceImpl(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    @Override
    public List<VetDTO> findAll() {
        List<Vet> vets = vetRepository.findAll();
        return vets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VetDTO findById(Integer id) throws VetNotFoundException {
        Vet vet = vetRepository.findById(id)
                .orElseThrow(() -> new VetNotFoundException("Vet not found with id: " + id));
        return toDTO(vet);
    }

    @Override
    public VetDTO create(VetDTO vetDTO) {
        Vet vet = toEntity(vetDTO);
        Vet saved = vetRepository.save(vet);
        return toDTO(saved);
    }

    @Override
    public VetDTO update(VetDTO vetDTO) throws VetNotFoundException {
        Vet vet = vetRepository.findById(vetDTO.getId())
                .orElseThrow(() -> new VetNotFoundException("Vet not found with id: " + vetDTO.getId()));
        vet.setFirstName(vetDTO.getFirstName());
        vet.setLastName(vetDTO.getLastName());
        Vet updated = vetRepository.save(vet);
        return toDTO(updated);
    }

    @Override
    public void delete(Integer id) throws VetNotFoundException {
        Vet vet = vetRepository.findById(id)
                .orElseThrow(() -> new VetNotFoundException("Vet not found with id: " + id));
        vetRepository.delete(vet);
    }

    private VetDTO toDTO(Vet vet) {
        return VetDTO.builder()
                .id(vet.getId())
                .firstName(vet.getFirstName())
                .lastName(vet.getLastName())
                .build();
    }

    private Vet toEntity(VetDTO vetDTO) {
        Vet vet = new Vet();
        vet.setFirstName(vetDTO.getFirstName());
        vet.setLastName(vetDTO.getLastName());
        return vet;
    }
}