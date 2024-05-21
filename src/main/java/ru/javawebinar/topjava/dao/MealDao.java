package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal get(int id);

    List<Meal> getAll();

    Meal create(Meal meal);

    Meal update(Meal meal);

    void delete(int id);
}
