package com.zubkov.kursovaya.controllers;

import com.zubkov.kursovaya.Main;
import com.zubkov.kursovaya.entities.*;
import com.zubkov.kursovaya.repositories.AddressRepo;
import com.zubkov.kursovaya.repositories.ClientRepo;
import com.zubkov.kursovaya.repositories.MealRepo;
import com.zubkov.kursovaya.repositories.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Controller
public class HomePageController
{
    @Autowired
    private ClientRepo clientRepo;

    //idClient, meal_ids
    private Map<Integer, List<Integer>> shoppingBasket = new HashMap<>();
    //IP, Client
    private Map<String, Client> authorizated = new HashMap<>();

    @Autowired
    private MealRepo mealRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AddressRepo addressRepo;

    @GetMapping("users")
    public String userList(HttpServletRequest request, Model model){
        String ip = getClientIp(request);
        if(authorizated.get(ip) != null)
        {
            if (authorizated.get(ip).getRole().equals("admin"))
            {
                model.addAttribute("users", clientRepo.findAll());
                return "userList";
            }
        }
        return "redirect:/account";
    }

    @PostMapping("users")
    public String deleteUser(@RequestParam Map<String, Object> map, HttpServletRequest request, Model model){
        String ip = getClientIp(request);
        if(authorizated.get(ip) != null)
        {
            if (authorizated.get(ip).getRole().equals("admin"))
            {
                System.out.println(Integer.parseInt((String) map.get("id")));
//                clientRepo.deleteClientById(Integer.parseInt((String) map.get("id")));
                clientRepo.delete(clientRepo.findClientById(Integer.parseInt((String) map.get("id"))));
                model.addAttribute("users", clientRepo.findAll());
                return "userList";
            }
        }
        return "redirect:/account";
    }

    @GetMapping("users/{id}")
    public String user(HttpServletRequest request, Model model, @PathVariable("id") int id){
        String ip = getClientIp(request);
        if(authorizated.get(ip) != null)
        {
            if (authorizated.get(ip).getRole().equals("admin"))
            {
                model.addAttribute("user", clientRepo.findClientById(id));
                return "user";
            }
        }
        return "redirect:/account";
    }

    @GetMapping("index")
    public String indexGet(){
        return "index";
    }

    @GetMapping("about")
    public String aboutGet(){
        return "about";
    }

    @GetMapping("menu")
    public String menuGet(Model model){
        List<Meal> mealsbreakfast = mealRepo.findMealsByMealtime(MealTime.BREAKFAST);
        model.addAttribute("mealsbreakfast", mealsbreakfast);
        List<Meal> mealsbrunch = mealRepo.findMealsByMealtime(MealTime.BRUNCH);
        model.addAttribute("mealsbrunch", mealsbrunch);
        List<Meal> mealslunch = mealRepo.findMealsByMealtime(MealTime.LUNCH);
        model.addAttribute("mealslunch", mealslunch);
        List<Meal> mealsdinner = mealRepo.findMealsByMealtime(MealTime.DINNER);
        model.addAttribute("mealsdinner", mealsdinner);
        return "menu";
    }

    @PostMapping("menu")
    public String menuPost(@RequestParam Map<String, Object> map, HttpServletRequest request, Model model){
        String ip = getClientIp(request);
        if( authorizated.get(ip) != null ){
            if(shoppingBasket.get(authorizated.get(ip).getId()) == null)
                shoppingBasket.put(authorizated.get(ip).getId(), new ArrayList<Integer>());
            String productName = (String) map.get("name");
//            System.out.println(getClientIp(request));
//            System.out.println(map.get("name"));
            shoppingBasket.get(authorizated.get(ip).getId()).add(mealRepo.findMealByName(productName).getId());
            System.out.println(authorizated.get(ip).getId()+ " " +shoppingBasket.get(authorizated.get(ip).getId()));
            return "redirect:/menu";
        }
        return "redirect:/authorization";
    }

    @GetMapping("blog")
    public String blogGet(){
        return "blog";
    }

    @GetMapping("authorization")
    public String authorizationGet(){
        return "authorization";
    }

    @PostMapping("authorization")
    public String authorizationPost(@RequestParam Map<String, Object> data, HttpServletRequest request, Model model){
        String email = (String)data.get("email");
        String password = (String) data.get("password");
        String ip = getClientIp(request);
        Client authClient = clientRepo.findClientByEmailAndAndPsswrd(email, password);
        if( authClient != null)
        {
            authorizated.put(ip, authClient);
//            System.out.println(authorizated.get(ip));

            return "redirect:/menu";
        }
        else{
            return "redirect:/authorization";
        }
    }

    @GetMapping("elements")
    public String elementsGet(){
        return "elements";
    }

    @GetMapping("registration")
    public String registrationGet(){
        return "registration";
    }

    @PostMapping("registration")
    public String registrationPost(@RequestParam Map<String, Object> request, Model model){
        System.out.println(request);
        String password = (String) request.get("password");
        String trypassword = (String) request.get("trypassword");
        if(password.equals(trypassword)){
            Client client = new Client((String) request.get("email"), password);
            clientRepo.saveAndFlush(client);
        }
        return "redirect:/authorization";
    }

    @GetMapping("shoppingbasket")
    public String shoppingbasketGet(HttpServletRequest request, Model model){
        String ip = getClientIp(request);
        if(authorizated.get(ip) != null)
        {
            if(shoppingBasket.get(authorizated.get(ip).getId())==null)
                shoppingBasket.put(authorizated.get(ip).getId(), new ArrayList<Integer>());
            Object[] a = shoppingBasket.get(authorizated.get(ip).getId()).toArray();
            Map<Integer, Meal> map = new HashMap<>();
            Map<Integer, Integer> counter = new HashMap<>();
            //Достает из корзины
            for (Object o : a)
            {
                int i = (int) o;
                if (map.get(i) == null)
                {
                    Meal meal = mealRepo.findMealById(i);
                    map.put(i, meal);
                    counter.put(i, 1);
                }
                else
                {
                    counter.put(i, counter.get(i) + 1);
                }
            }
            model.addAttribute("map", map);
            model.addAttribute("counter", counter);
            return "shoppingbasket";
        }else{
            return "redirect:/authorization";
        }
    }

    @PostMapping("shoppingbasket")
    public String shoppingbasketPost(@RequestParam Map<String, Object> map, HttpServletRequest request, Model model){
        if(map.keySet().size() > 0)
        {
            String ip = getClientIp(request);
            List<Integer> tmp = new ArrayList();
//            System.out.println(map);
//            System.out.println(map.keySet());
//            System.out.println(map.values());
            int size = 0;
            for (Object value : map.values())
            {
                size += Integer.parseInt((String) value);
            }
//            System.out.println(size);
            int[] result = new int[size];
            float price = 0;
            for (String key : map.keySet())
            {
//            int meal_id = Integer.parseInt(key.substring(key.length()-1));
                int meal_id = Integer.parseInt(key.replaceAll("count", ""));
                size = Integer.parseInt((String) map.get(key));
//                System.out.println(meal_id + " " + (map.get(key)));
                for (int i = 0; i < size; i++)
                {
                    tmp.add(meal_id);
                    price += mealRepo.findMealById(meal_id).getPrice();
                }
            }
            for (int i = 0; i < tmp.size(); i++)
            {
                result[i] = tmp.get(i);
            }
            Arrays.stream(result).forEach(n -> System.out.print(n + " "));
            Client client = authorizated.get(ip);
            Order order = new Order(result, price, client);
            client.AddOrder(order);
            shoppingBasket.put(client.getId(), new ArrayList<Integer>());
            clientRepo.saveAndFlush(client);
            return "redirect:/index";
        }
        return "shoppingbasket";
    }

    @GetMapping("account")
    public String accountGet(HttpServletRequest request, Model model){
        String ip = getClientIp(request);
        Client client = authorizated.get(ip);
        if( client != null ){
            Address address = client.getFk_address();
            if( address == null )
                client.setFk_address(new Address(" ", " ", " ", " "));
            model.addAttribute("client", client);
            List<Order> orders = client.getOrders();
            Map<Integer, Map<String, Integer>> ordersMap = new HashMap();
            Collections.reverse(orders);
            for (Order order : orders)
            {
                int[] meals_id = order.getMeals_id();
                Map<String, Integer> localOrderMap = new HashMap<>();
                for (int meal_id : meals_id)
                {
                    if(localOrderMap.get(mealRepo.findMealById(meal_id).getName()) == null)
                        localOrderMap.put(mealRepo.findMealById(meal_id).getName(), 0);
                    localOrderMap.put(mealRepo.findMealById(meal_id).getName(), localOrderMap.get(mealRepo.findMealById(meal_id).getName())+1);
                }
//                System.out.println(localOrderMap);
                ordersMap.put(order.getOrder_id(), localOrderMap);
            }
//            System.out.println(ordersMap);
            model.addAttribute("orders", orders);
            model.addAttribute("ordersMap", ordersMap);
            return "account";
        }
        return "redirect:/authorization";
    }

    @PostMapping("account")
    public String accountPost(@RequestParam Map<String, Object> map, HttpServletRequest request, Model model){
        String ip = getClientIp(request);
        Client client = authorizated.get(ip);
        System.out.println(map);
        if( client != null){
            if(map.get("type").equals("edit")){
                Address address = client.getFk_address();
                if( address == null )
                    address = new Address();
                client.setEmail((String) map.get("email"));
                client.setPsswrd((String) map.get("password"));
                address.setCity((String) map.get("city"));
                address.setStreet((String) map.get("street"));
                address.setHouse((String) map.get("house"));
                address.setFlat((String) map.get("flat"));
                client.setFk_address(address);
                clientRepo.saveAndFlush(client);
                System.out.println("Saved");
            }
            return "redirect:/account";
        }
        return "redirect:/authorization";
    }

    private final String LOCALHOST_IPV4 = "127.0.0.1";
    private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    public String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!StringUtils.isEmpty(ipAddress)
                && ipAddress.length() > 15
                && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }
}