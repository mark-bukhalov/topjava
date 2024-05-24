package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAllByUser(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(Integer mealId) {
        log.info("get {}", mealId);
        return service.get(mealId, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(Integer mealId) {
        log.info("delete {}", mealId);
        service.delete(mealId, SecurityUtil.authUserId());
    }

    public void update(Integer mealId, Meal meal) {
        log.info("update {} with id={}", meal, mealId);
        ValidationUtil.assureIdConsistent(meal, mealId);
        service.update(meal, SecurityUtil.authUserId());
    }


}