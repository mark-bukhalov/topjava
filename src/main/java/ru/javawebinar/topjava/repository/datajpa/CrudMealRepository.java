package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Meal getByIdAndUserId(int id, int userId);

    List<Meal> deleteMealByIdAndUserId(int id, int userId);

    List<Meal> getAllByUserID(int userId, Sort sort);
}
