package com.example.challenge.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Usuários")
@RestController
@RequestMapping("api/v1/")
public class IndexController {

    @ApiOperation("Retorna os dados do usuário logado.")
    @GetMapping("userInfo")
    public UserDetails userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
}
