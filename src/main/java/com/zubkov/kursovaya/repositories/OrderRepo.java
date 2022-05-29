package com.zubkov.kursovaya.repositories;

import com.zubkov.kursovaya.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer>
{
    
}
