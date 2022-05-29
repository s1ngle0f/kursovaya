package com.zubkov.kursovaya;

import com.zubkov.kursovaya.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {

    }

    public static Session GetSession(){
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Meal.class);
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            return session;
        }catch (Exception e){
            return null;
        }
    }

    public static void DoInSession(IDo iDo) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Meal.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            iDo.Do(session);
            session.getTransaction().commit();
        }
    }

    public static void DoInSessionWithoutCommit(IDo iDo) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Meal.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            iDo.Do(session);
        }
    }

    public static List<Client> GetClientByAllName(String firstname, String secondname){
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Meal.class);
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            return session.createSQLQuery(String.format("SELECT * FROM clients WHERE firstname = '%s' AND secondname = '%s'", firstname, secondname))
                    .addEntity(Client.class)
                    .list();
        }
    }
}
