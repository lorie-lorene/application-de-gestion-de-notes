package com.app.sygen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.sygen.entities.PvUe;
import com.app.sygen.entities.Ue;

import java.util.List;
import java.time.LocalDate;



public interface PvUeRepository extends JpaRepository<PvUe,Integer>{
    PvUe findByUe(Ue ue);
    PvUe findByAnneeAndSemestreAndUe(String annee, int semestre, Ue ue);
    
}
