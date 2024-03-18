package com.app.sygen.repositories;

import org.springframework.stereotype.Repository;

import com.app.sygen.entities.Enseignant;
// import com.app.sygen.entities.Participe;
import com.app.sygen.entities.Users;

// import java.util.List;

@Repository
public interface EnseignantRepository extends AppRepository<Enseignant, Long>{
   Enseignant findByUser(Users user);
}
