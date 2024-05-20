package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal get(int id);

    List<Meal> getAll();

    void save(Meal meal);

    void update(Meal meal);

    void delete(int id);
}
