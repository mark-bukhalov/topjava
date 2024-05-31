package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final Meal userMeal1 = new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак [User]", 500);
    public static final Meal userMeal2 = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед [User]", 1000);
    public static final Meal userMeal3 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин [User]", 500);
    public static final Meal userMeal4 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед [User]", 500);

    public static final Meal adminMeal1 = new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение [Admin]", 100);
    public static final Meal adminMeal2 = new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак [Admin]", 1000);
    public static final Meal adminMeal3 = new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед [Admin]", 500);
    public static final Meal adminMeal4 = new Meal(START_SEQ + 10, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин [Admin]", 410);

    public static Meal getUpdated() {
        Meal updateMeal = new Meal(userMeal1);
        updateMeal.setDateTime(LocalDateTime.of(2024, 5, 4, 13, 0));
        updateMeal.setDescription("update desc");
        updateMeal.setCalories(999);
        return updateMeal;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
