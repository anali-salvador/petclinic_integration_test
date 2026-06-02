package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<OwnerDTO> findAll() {
        List<Owner> owners = ownerRepository.findAll();
        return owners.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OwnerDTO findById(Long id) throws OwnerNotFoundException {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + id));
        return toDTO(owner);
    }

    @Override
    public OwnerDTO create(OwnerDTO ownerDTO) {
        Owner owner = toEntity(ownerDTO);
        Owner saved = ownerRepository.save(owner);
        return toDTO(saved);
    }

    @Override
    public OwnerDTO update(OwnerDTO ownerDTO) throws OwnerNotFoundException {
        Owner owner = ownerRepository.findById(ownerDTO.getId())
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + ownerDTO.getId()));
        owner.setFirstName(ownerDTO.getFirstName());
        owner.setLastName(ownerDTO.getLastName());
        owner.setAddress(ownerDTO.getAddress());
        owner.setCity(ownerDTO.getCity());
        owner.setTelephone(ownerDTO.getTelephone());
        Owner updated = ownerRepository.save(owner);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) throws OwnerNotFoundException {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with id: " + id));
        ownerRepository.delete(owner);
    }

    private OwnerDTO toDTO(Owner owner) {
        return OwnerDTO.builder()
                .id(owner.getId())
                .firstName(owner.getFirstName())
                .lastName(owner.getLastName())
                .address(owner.getAddress())
                .city(owner.getCity())
                .telephone(owner.getTelephone())
                .build();
    }

    private Owner toEntity(OwnerDTO ownerDTO) {
        Owner owner = new Owner();
        owner.setFirstName(ownerDTO.getFirstName());
        owner.setLastName(ownerDTO.getLastName());
        owner.setAddress(ownerDTO.getAddress());
        owner.setCity(ownerDTO.getCity());
        owner.setTelephone(ownerDTO.getTelephone());
        return owner;
    }
}