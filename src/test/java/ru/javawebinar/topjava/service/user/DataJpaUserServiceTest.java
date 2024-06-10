package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeals() {
        User withMealsDb = service.getWithMeals(USER_ID);
        User withMeals = new User(user);
        withMeals.setMeals(MealTestData.meals);
        USER_WITH_MEALS_MATCHER.assertMatch(withMealsDb, withMeals);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void getWithMealsMealsIsEmpty() {
        User withMealsDb = service.getWithMeals(GUEST_ID);
        User withMeals = new User(guest);
        withMeals.setMeals(new ArrayList<>());
        USER_WITH_MEALS_MATCHER.assertMatch(withMealsDb, withMeals);
    }
}
