package com.example.challenge.api.assembler;

import com.example.challenge.api.dto.VotoDTO;
import com.example.challenge.domain.entities.Voto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VotoDtoAssembler {
    @Autowired
    private ModelMapper modelMapper;

    public VotoDTO votoToDto(Voto voto) {
        return modelMapper.map(voto, VotoDTO.class);
    }

    public List<VotoDTO> toCollectionOfDto(List<Voto> votos) {
        return votos.stream().map(this::votoToDto).collect(Collectors.toList());
    }
}
