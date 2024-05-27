package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public Meal get(int mealId, Integer userId) {
        Meal meal = ValidationUtil.checkNotFoundWithId(repository.get(mealId), mealId);
        ValidationUtil.userIsOwner(meal, userId);
        return meal;
    }

    public Meal create(Meal meal, int userId) {
        meal.setUserId(userId);
        return repository.save(meal);
    }

    public void delete(int mealId, int userId) {
        Meal meal = ValidationUtil.checkNotFoundWithId(repository.get(mealId), mealId);
        ValidationUtil.userIsOwner(meal, userId);
        repository.delete(mealId);
    }

    public void update(Meal meal, int userId) {
        Meal oldMeal = ValidationUtil.checkNotFoundWithId(repository.get(meal.getId()), meal.getId());
        ValidationUtil.userIsOwner(oldMeal, userId);
        meal.setUserId(userId);
        repository.save(meal);
    }
}