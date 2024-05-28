package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, SecurityUtil.USER));
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);

        Map<Integer, Meal> userRep = getUserRep(userId);
        return userRep != null && userRep.remove(id) != null;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> userRep = repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
            userRep.put(meal.getId(), meal);
            return meal;
        }

        Map<Integer, Meal> userRep = getUserRep(userId);
        return userRep == null ? null : userRep.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);

        Map<Integer, Meal> userRep = getUserRep(userId);
        return userRep == null ? null : userRep.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll {}", userId);

        Map<Integer, Meal> userRep = getUserRep(userId);

        if (userRep == null) {
            return Collections.emptyList();
        } else {
            return userRep.values().stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Meal> getDateFilteredAll(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getDateFilteredAll {} {} {}", userId, startDate, endDate);

        return getAll(userId).stream()
                .filter(value -> DateTimeUtil.isBetweenHalfOpen(value.getDate(), startDate, endDate == LocalDate.MAX ? endDate : endDate.plusDays(1)))
                .collect(Collectors.toList());

    }

    private Map<Integer, Meal> getUserRep(int userId) {
        return repository.get(userId);
    }
}

