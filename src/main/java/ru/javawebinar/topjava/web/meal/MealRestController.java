package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Controller
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAllByUser(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(Integer mealId) {
        return service.get(mealId, SecurityUtil.authUserId());
    }

    public void delete(Integer mealId) {
        service.delete(mealId, SecurityUtil.authUserId());
    }

    public void update(Integer mealId, Meal meal) {
        ValidationUtil.assureIdConsistent(meal, mealId);
        service.update(meal, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        ValidationUtil.checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }
}