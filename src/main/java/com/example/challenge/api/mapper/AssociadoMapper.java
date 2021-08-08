package com.example.challenge.api.mapper;

import com.example.challenge.api.request.AssociadoRequest;
import com.example.challenge.domain.entities.Associado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssociadoMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Associado map(AssociadoRequest request) {
        return modelMapper.map(request, Associado.class);
    }
}
