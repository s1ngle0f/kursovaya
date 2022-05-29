package com.zubkov.kursovaya.repositories;

import com.zubkov.kursovaya.entities.Address;
import com.zubkov.kursovaya.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long>
{
    Address findAddressByClient(Client client);
}
