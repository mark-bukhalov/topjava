package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAllFilter {} {} {} {}", startDate, endDate, startTime, endTime);
        return service.getFilteredAll(SecurityUtil.authUserId(),
                SecurityUtil.authUserCaloriesPerDay(),
                Optional.ofNullable(startDate).orElse(LocalDate.MIN),
                Optional.ofNullable(endDate).orElse(LocalDate.MAX),
                Optional.ofNullable(startTime).orElse(LocalTime.MIN),
                Optional.ofNullable(endTime).orElse(LocalTime.MAX));
    }

    public Meal get(int mealId) {
        log.info("get {}", mealId);
        return service.get(mealId, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int mealId) {
        log.info("delete {}", mealId);
        service.delete(mealId, SecurityUtil.authUserId());
    }

    public void update(int mealId, Meal meal) {
        log.info("update {} with id={}", meal, mealId);
        ValidationUtil.assureIdConsistent(meal, mealId);
        service.update(meal, SecurityUtil.authUserId());
    }
}