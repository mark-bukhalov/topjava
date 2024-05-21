package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
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

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final int CALORIES_PER_DAY = 1500;
    private static final MealDao mealDao = new MemoryMealDao();
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("RequestURL:{}, Parameters:{}, Method:GET", req.getRequestURL(), req.getQueryString());

        String action = req.getParameter("action");

        switch (action == null ? "all" : action.toLowerCase()) {
            case ("delete"):
                int id = Integer.parseInt(req.getParameter("id"));
                mealDao.delete(id);
                resp.sendRedirect(req.getContextPath() + "/meals");
                log.debug("delete id:{}", id);
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
                log.debug("Get all");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("RequestURL:{}, Parameters:{}, Method:POST", req.getRequestURL(), req.getQueryString());
        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));

        if (meal.getId() == null) {
            meal = mealDao.create(meal);
            log.debug("create id:{}", meal.getId());
        } else {
            mealDao.update(meal);
            log.debug("update id:{}", meal.getId());
        }

        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
