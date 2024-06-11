package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATETIME = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew() || get(meal.getId(), userId) != null) {
            meal.setUser(userRepository.getReferenceById(userId));
            return crudRepository.save(meal);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteMealByIdAndUser(id, userRepository.getReferenceById(userId)) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUser(id, userRepository.getReferenceById(userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllByUser(userRepository.getReferenceById(userId), SORT_DATETIME);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(userId, startDateTime, endDateTime);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return crudRepository.getWithUser(id, userId);

//        Изначально сделал таким образом, но тут возникла проблема в тестах, т как Hibernate делает прокси класс
//        field/property 'user' differ:
//        - actual value  : User {id=100000, email=user@yandex.ru, name=User, enabled=true, roles=[USER], caloriesPerDay=2000} (User$HibernateProxy$TrFYjrSE@3b80bb63)
//        - expected value: User{id=100000, email=user@yandex.ru, name=User, enabled=true, roles=[USER], caloriesPerDay=2000} (User@17ebbf1e)

//        UPD решил проблему с прокси через meal.setUser(Hibernate.unproxy(meal.getUser(),User.class));
//        Решил оставить решение через return crudRepository.getWithUser(id, userId);

//        Есть еще вариант с meal.setUser(userRepository.get(userId)).

//        Meal meal = get(id, userId);
//        if (meal == null) {
//            return null;
//        }
//        meal.getUser().getName();
//        return meal;
    }
}
