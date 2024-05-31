package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal mealDb = service.get(userMeal1.getId(), USER_ID);
        assertMatch(mealDb, userMeal1);
    }

    @Test
    public void getNotOwner() {
        assertThrows(NotFoundException.class, () -> service.get(userMeal1.getId(), ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(adminMeal1.getId(), ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(adminMeal1.getId(), ADMIN_ID));
    }

    @Test
    public void deleteNotOwner() {
        assertThrows(NotFoundException.class, () -> service.get(adminMeal1.getId(), USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> mealsDb = service.getBetweenInclusive(LocalDate.of(2020, 1, 30), LocalDate.of(2020, 1, 30), USER_ID);
        assertMatch(mealsDb, userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void getAll() {
        List<Meal> mealsDb = service.getAll(USER_ID);
        assertMatch(mealsDb, userMeal4, userMeal3, userMeal2, userMeal1);
    }

    @Test
    public void update() {
        Meal updateMeal = getUpdated();
        service.update(updateMeal, USER_ID);
        assertMatch(service.get(updateMeal.getId(), USER_ID), updateMeal);
    }

    @Test
    public void updateNotOwner() {
        Meal updateMeal = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updateMeal, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2024, 5, 4, 13, 0), "NEW", 999);

        Meal createdMeal = service.create(newMeal, USER_ID);
        newMeal.setId(createdMeal.getId());
        assertMatch(createdMeal, newMeal);
        assertMatch(service.get(createdMeal.getId(), USER_ID), newMeal);
    }

    @Test
    public void duplicateUserIdDatetime() {
        assertThrows(DataAccessException.class, () -> service.create(new Meal(userMeal1.getDateTime(), "duplicate", 999), USER_ID));
    }
}