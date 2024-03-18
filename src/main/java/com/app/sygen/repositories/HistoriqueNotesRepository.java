package com.app.sygen.repositories;

import org.springframework.stereotype.Repository;

import com.app.sygen.entities.Evaluation;
import com.app.sygen.entities.HistoriqueNote;

@Repository
public interface HistoriqueNotesRepository extends AppRepository<HistoriqueNote, Long> {
    
}
