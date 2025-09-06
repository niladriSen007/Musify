package com.niladri.musify_backend_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class MusifyController {

//    private final IMusifyService musifyService;

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }


}
