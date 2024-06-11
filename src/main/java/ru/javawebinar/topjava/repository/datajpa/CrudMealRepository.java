package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Meal getByIdAndUser(int id, User user);

    @Transactional
    int deleteMealByIdAndUser(int id, User user);

    List<Meal> getAllByUser(User user, Sort sort);

    @Query("""
                          SELECT m
                            FROM Meal m 
                           WHERE m.user.id=:userId
                             AND m.dateTime >= :startDateTime
                             AND m.dateTime < :endDateTime
                        ORDER BY m.dateTime DESC
            """)
    List<Meal> getBetweenHalfOpen(@Param("userId") int userId, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    @Query(" SELECT m FROM  Meal m JOIN FETCH m.user  WHERE m.id=:id AND m.user.id=:userId")
    Meal getWithUser(@Param("id") int id, @Param("userId") int userId);
}
