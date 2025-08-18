package com.ninehub.authentication.controller;

import com.ninehub.authentication.entity.Avis;
import com.ninehub.authentication.service.AvisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("avis")
public class AvisController {

    private final AvisService avisService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public void createAvis(@RequestBody Avis avis){

        this.avisService.createAvis(avis);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Avis>> getAllAvis(){
        return new ResponseEntity<>(avisService.getAllAvis(), HttpStatus.OK);
    }
}
