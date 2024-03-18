package com.app.sygen.repositories;

import com.app.sygen.entities.Ue;


public interface UeRepository extends AppRepository<Ue, Long>{
    
    Ue findByCode(String code);
    Ue findFirstByCode(String code);

}
