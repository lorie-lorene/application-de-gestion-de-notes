package com.app.sygen.repositories;

import com.app.sygen.entities.Jury;
import com.app.sygen.entities.SeanceDeliberation;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SeanceDeliberationRepository extends AppRepository<SeanceDeliberation, Long>{
    SeanceDeliberation  findByCreateJury(Jury createJury);
    SeanceDeliberation findByCle(String cle);
}
