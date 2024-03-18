package com.app.sygen.repositories;

import org.springframework.stereotype.Repository;

import com.app.sygen.entities.Evaluation;
import com.app.sygen.entities.StatutNote;

@Repository
public interface StatutNoteRepository extends AppRepository<StatutNote, Long>{
    StatutNote findFirstByEvaluation(Evaluation evale);
}
