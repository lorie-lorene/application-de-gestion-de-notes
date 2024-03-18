package com.app.sygen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Interface de base dont heritera tout autre interface de l'application
 */
@NoRepositoryBean
public interface AppRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, ID>
{

}
