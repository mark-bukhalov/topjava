package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealMemoryStorage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class MealServlet extends HttpServlet {
    private static final int CALORIES_PER_DAY = 1500;
    private static final MealDao mealDao = new MealMemoryStorage();

    @Override
    public void init() throws ServletException {
        mealDao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealDao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealDao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealDao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealDao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealDao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            req.setAttribute("meals", MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), CALORIES_PER_DAY));
            req.getRequestDispatcher("meals.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            mealDao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/meals");
        } else if (action.equalsIgnoreCase("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Meal meal = mealDao.get(id);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("meal.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("insert")) {
            req.setAttribute("meal", new Meal());
            req.getRequestDispatcher("meal.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Meal meal = new Meal();
        meal.setDescription(req.getParameter("description"));
        meal.setCalories(Integer.parseInt(req.getParameter("calories")));
        meal.setDateTime(LocalDateTime.parse(req.getParameter("dateTime")));
        int id = Integer.parseInt(req.getParameter("id"));

        if (id == -1) {
            mealDao.save(meal);
        } else {
            meal.setId(id);
            mealDao.update(meal);
        }

        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
