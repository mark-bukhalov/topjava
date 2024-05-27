package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            //Тестирование MemoryUserRepository
            InMemoryUserRepository userRep = appCtx.getBean(InMemoryUserRepository.class);

            User user1 = new User(null, "Ivan", "ivan@mail.ru", "qwerty", Role.USER);
            User user2 = new User(null, "Ivan", "abc@mail.ru", "qwerty", Role.USER);
            User user3 = new User(null, "Mark", "mark@mail.ru", "qwerty", Role.ADMIN);

            userRep.save(user1);
            userRep.save(user2);
            userRep.save(user3);

            System.out.println(userRep.getAll());
            System.out.println(userRep.getByEmail("ivan@mail.ru"));
            System.out.println(userRep.getByEmail("qqq@mail.ru"));

            //Тестирование MealRestController
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println(mealRestController.getAll());
        }
    }
}
