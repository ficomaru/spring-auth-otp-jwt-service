package com.ninehub.authentication.service;

import com.ninehub.authentication.entity.Avis;
import com.ninehub.authentication.entity.User;
import com.ninehub.authentication.repository.AvisRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AvisService {
    private final AvisRepository avisRepository;

    public void createAvis(Avis avis){
        // Maintenant que le user est connecter, on recupere le contexte pour qu'il puisse effectuer toute les actions sur le site
        // Avoir l'utilisateur connecter
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUser((User) user);
        this.avisRepository.save(avis);
    }

    public List<Avis> getAllAvis(){
        return this.avisRepository.findAll();
    }
}
