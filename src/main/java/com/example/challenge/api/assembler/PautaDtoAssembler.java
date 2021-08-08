package com.example.challenge.api.assembler;

import com.example.challenge.api.dto.PautaDTO;
import com.example.challenge.domain.entities.Pauta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PautaDtoAssembler {
    @Autowired
    private ModelMapper modelMapper;

    public PautaDTO pautaToDto(Pauta pauta) {
        return modelMapper.map(pauta, PautaDTO.class);
    }

    public List<PautaDTO> toCollectionOfDto(List<Pauta> pautas) {
        return pautas.stream().map(this::pautaToDto).collect(Collectors.toList());
    }
}
