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
        return ValidationUtil.checkNotFoundWithId(repository.get(mealId, userId), mealId);
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int mealId, int userId) {
        ValidationUtil.checkNotFoundWithId(repository.delete(mealId, userId), mealId);
    }

    public void update(Meal meal, int userId) {
        ValidationUtil.checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}