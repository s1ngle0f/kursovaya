package com.zubkov.kursovaya;

import com.zubkov.kursovaya.entities.*;
import com.zubkov.kursovaya.repositories.AddressRepo;
import com.zubkov.kursovaya.repositories.ClientRepo;
import com.zubkov.kursovaya.repositories.MealRepo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@SpringBootTest
@Slf4j
@Aspect
class KursovayaApplicationTests {

	@Autowired
	private ClientRepo clientRepo;

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private MealRepo mealRepo;

	@Test
	void contextLoads() {
		System.out.println(clientRepo.findAll());
	}

	@Test
	void GetClient(){
		Client client = clientRepo.findClientById(54);
		System.out.println(client);
	}

	@Test
	void SetAddress(){
		Client client = clientRepo.findClientById(54);
		Address address = new Address("Moscow", "Samuila Marshaka", "14", "78");
		client.setFk_address(address);
		clientRepo.saveAndFlush(client);
	}

	@Test
	void GetAddressByClient(){
		Client client = clientRepo.findClientById(54);
		Address address = addressRepo.findAddressByClient(client);
		System.out.println(address);
	}

	@Test
	void SetAddressBySession(){
		Client client = clientRepo.findClientById(54);
		System.out.println(client);
		Address address = new Address("Moscow", "Samuila Marshaka", "14", "78");
		client.setFk_address(address);
		Main.DoInSession(session -> {
			session.update(client);
		});
	}

	@Test
	void CreateNewOrder(){
		Client client = clientRepo.findClientById(54);
		Order order = new Order(new int[]{1, 1, 1, 1, 2, 2}, 1450, client);
		client.AddOrder(order);
		clientRepo.saveAndFlush(client);
	}

	@Test
	void GetMeals(){
		System.out.println(mealRepo.findMealsByMealtime(MealTime.BREAKFAST));
	}

	@Test
	void AddMeal(){
		mealRepo.saveAndFlush(new Meal("Milk", 300, "Milk", MealTime.DINNER));
		mealRepo.saveAndFlush(new Meal("Water", 100, "Water", MealTime.DINNER));
		mealRepo.saveAndFlush(new Meal("Spagetti", 100, "From Italia", MealTime.DINNER));
	}

	@Test
	void testNameThyme(){
		String name = "hleb";
		System.out.println("'" + name + "'");
		System.out.println( name );
	}

	@Test
	void prac17(){
		Main.DoInSessionWithoutCommit(session -> {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Client> clientCriteriaQuery = builder.createQuery(Client.class);
			Root<Client> root = clientCriteriaQuery.from(Client.class);
//			clientCriteriaQuery.select(root).orderBy(builder.asc(root.get("Mihail")));
			clientCriteriaQuery.select(root);
			Query query = session.createQuery(clientCriteriaQuery);
			System.out.println(query.getResultList());
		});
	}
}
