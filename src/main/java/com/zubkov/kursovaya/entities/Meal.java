package com.zubkov.kursovaya.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Meal implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private float price;

    private String description;

    private MealTime mealtime;

    public Meal(String name, float price)
    {
        this.name = name;
        this.price = price;
    }

    public Meal(String name, float price, MealTime mealtime)
    {
        this.name = name;
        this.price = price;
        this.mealtime = mealtime;
    }

    public Meal(String name, float price, String description, MealTime mealtime)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.mealtime = mealtime;
    }

    public String GetNameForThymeLeaf(){
        return "'" + name + "'";
    }
}