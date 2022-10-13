package com.zubkov.kursovaya.entities;

import lombok.*;
import com.zubkov.kursovaya.Main;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
//@Data
@Getter
@Setter
//@ToString
@Table(name = "clients")
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "email")
    private String email;
    @Column(name = "psswrd")
    private String psswrd;

    @Column(name = "role")
    private String role;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_address")
    private Address fk_address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    List<Order> orders;

    public void AddOrder(Order order){
        orders.add(order);
    }

    public Client(String email, String psswrd)
    {
        this.email = email;
        this.psswrd = psswrd;
        this.role = "user";
    }

    @Builder
    public Client(String email, String psswrd, Address address) {
        this.email = email;
        this.psswrd = psswrd;
        fk_address = address;
        this.role = "user";
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", psswrd='" + psswrd + '\'' +
                ", fk_address=" + fk_address +
                ", orders=" + orders +
                '}';
    }

    //      Remake!!! Cause exist session.update(Object)
//    public void SetNewAddress(Address address){
//        Main.DoInSession((session -> {
//            List<Address> addressList = session.createSQLQuery(String.format("SELECT * FROM address WHERE city = '%s' AND" +
//                            " street = '%s' AND house = '%s' AND flat = '%s'", address.getCity(), address.getStreet(),
//                    address.getHouse(), address.getFlat())).addEntity(Address.class).list();
//            if(addressList.size() <= 0){
//                address.setId(session.createSQLQuery("SELECT * FROM address").list().size());
//                this.fk_address = address.getId();
//                session.save(address);
//                session.update(this);
//            }else{
//                this.fk_address = addressList.get(0).getId();
//                session.update(this);
//            }
//        }));
//    }

    public Client Delete(){
        Main.DoInSession(session -> {
            session.delete(this);
        });
        return this;
    }
}



