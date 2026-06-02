package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;

import java.util.List;

public interface VisitService {

    List<VisitDTO> findAll();

    VisitDTO findById(Long id) throws VisitNotFoundException;

    VisitDTO create(VisitDTO visitDTO);

    VisitDTO update(VisitDTO visitDTO) throws VisitNotFoundException;

    void delete(Long id) throws VisitNotFoundException;
}