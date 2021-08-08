package com.example.challenge.api.assembler;

import com.example.challenge.api.dto.AssociadoDTO;
import com.example.challenge.domain.entities.Associado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssociadoDtoAssembler {
    @Autowired
    private ModelMapper modelMapper;

    public AssociadoDTO associadoToDto(Associado associado) {
        return modelMapper.map(associado, AssociadoDTO.class);
    }

    public List<AssociadoDTO> toCollectionOfDto(List<Associado> associados) {
        return associados.stream().map(this::associadoToDto).collect(Collectors.toList());
    }
}
