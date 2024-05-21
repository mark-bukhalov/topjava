package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MemoryMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MealServlet extends HttpServlet {
    private static final int CALORIES_PER_DAY = 1500;
    private static final MealDao mealDao = new MemoryMealDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action == null ? "all" : action.toLowerCase()) {
            case ("delete"):
                int id = Integer.parseInt(req.getParameter("id"));
                mealDao.delete(id);
                resp.sendRedirect(req.getContextPath() + "/meals");
                break;
            case ("edit"):
                req.setAttribute("action", "edit");
                req.setAttribute("meal", mealDao.get(Integer.parseInt(req.getParameter("id"))));
                req.getRequestDispatcher("meal.jsp").forward(req, resp);
                break;
            case ("insert"):
                req.setAttribute("action", "insert");
                req.setAttribute("meal", new Meal());
                req.getRequestDispatcher("meal.jsp").forward(req, resp);
            case ("all"):
                req.setAttribute("meals", MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX,
                        CALORIES_PER_DAY));
                req.getRequestDispatcher("meals.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));

        if (meal.getId() == null) {
            mealDao.create(meal);
        } else {
            mealDao.update(meal);
        }

        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
