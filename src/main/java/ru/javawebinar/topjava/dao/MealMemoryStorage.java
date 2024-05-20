package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealIdCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MealMemoryStorage implements MealDao {
    private static final ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public Meal get(int id) {
        return meals.getOrDefault(id, new Meal());
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public void save(Meal meal) {
        meal.setId(MealIdCounter.generateId());
        meals.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
