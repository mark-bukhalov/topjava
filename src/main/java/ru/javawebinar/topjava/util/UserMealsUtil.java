package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(12, 0), 900);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(12, 0), 900));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sumCaloriesPerDay = new HashMap<>();

        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                sumCaloriesPerDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            }
        }

        List<UserMealWithExcess> resultList = new ArrayList<>();

        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        sumCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return resultList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sumCaloriesPerDay = meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        sumCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
