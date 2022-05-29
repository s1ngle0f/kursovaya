package com.zubkov.kursovaya.repositories;

import com.zubkov.kursovaya.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<Client, Integer> //, ClientCustomRepo
{
    List<Client> findClientsByEmail(String email);
    Client findClientById(int id);

    Client findClientByEmailAndAndPsswrd(String email, String psswrd);
}
