package com.example.challenge.api.mapper;

import com.example.challenge.api.request.PautaRequest;
import com.example.challenge.domain.entities.Pauta;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PautaMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Pauta map(PautaRequest request) {
        return modelMapper.map(request, Pauta.class);
    }
}
