package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATETIME = Sort.by(Sort.Direction.DESC, "dateTime");
    private final CrudMealRepository crudRepository;
    @PersistenceContext
    private EntityManager em;

    public DataJpaMealRepository(CrudMealRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew() || get(meal.getId(), userId) != null) {
            meal.setUser(em.getReference(User.class, userId));
            return crudRepository.save(meal);
        } else {
            return null;
        }
    }

    @Override
    @Transactional //https://stackoverflow.com/a/58861890
    public boolean delete(int id, int userId) {
        return crudRepository.deleteMealByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllByUserId(userId, SORT_DATETIME);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThan(userId, startDateTime, endDateTime, SORT_DATETIME);
    }
}
