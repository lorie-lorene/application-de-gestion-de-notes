package com.app.sygen.repositories;

import org.springframework.stereotype.Repository;

import com.app.sygen.entities.Users;
import java.util.List;


@Repository
public interface UserRepository extends AppRepository<Users, Long>
{
	Users findByLoginAndPassword(String login, String password);
    Users findByLogin(String login);
    Users  findByUsername(String username);
}
