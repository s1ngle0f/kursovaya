package com.zubkov.kursovaya.entities;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.*;
import com.zubkov.kursovaya.Main;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.dialect.PostgreSQL10Dialect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Data
@Getter
@Setter
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@TypeDefs({
        @TypeDef(
                defaultForType = int[].class,
                typeClass = IntArrayType.class
        )
})
public class Order implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;

    private float price;
    @Column(name = "meals_id", columnDefinition = "integer[]")
    private int[] meals_id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Order(int[] meals_id)
    {
        this.price = 0;
        Main.DoInSession(session -> {
            for(int i = 0; i < meals_id.length; i++){
                Meal meal = session.get(Meal.class, meals_id[i]);
                this.price += meal.getPrice();
            }
        });
        this.meals_id = meals_id;
    }

    public Order(int[] meals_id, float price)
    {
        this.price = price;
        this.meals_id = meals_id;
    }

    public Order(int[] meals_id, float price, Client client)
    {
        this.price = price;
        this.meals_id = meals_id;
        this.client = client;
    }

    @Override
    public String toString()
    {
        return "Order{" +
                "order_id=" + order_id +
                ", price=" + price +
                ", meals_id=" + Arrays.toString(meals_id) +
                '}';
    }

    //    public Order(float price, Integer[] meals_id, Client client)
//    {
//        this.price = price;
//        this.meals_id = meals_id;
//        this.client = client;
//    }

//    public Order AddMealsId(Integer meal_id){
//        meals_id.add(meal_id);
//        return this;
//    }
}
