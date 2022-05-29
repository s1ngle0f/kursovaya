package com.zubkov.kursovaya.repositories;

import com.zubkov.kursovaya.entities.Meal;
import com.zubkov.kursovaya.entities.MealTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepo extends JpaRepository<Meal, Integer>
{
    Meal findMealById(Integer id);
    List<Meal> findMealsByMealtime(MealTime mealtime);
    Meal findMealByName(String name);
}
