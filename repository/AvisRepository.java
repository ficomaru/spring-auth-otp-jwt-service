package com.ninehub.authentication.repository;

import com.ninehub.authentication.entity.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
//    List<Avis> getAllAvis();
}
