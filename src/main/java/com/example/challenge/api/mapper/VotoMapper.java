package com.example.challenge.api.mapper;

import com.example.challenge.api.request.VotoRequest;
import com.example.challenge.domain.entities.Voto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VotoMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Voto map(VotoRequest request) {
        return modelMapper.map(request, Voto.class);
    }
}
