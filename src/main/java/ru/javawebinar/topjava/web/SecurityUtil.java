package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    public static final int USER = 1;
    public static final int ADMIN = 2;
    private static int authUserId = USER;


    public static int authUserId() {
        return authUserId;
    }

    public static void setAuthUserId(int id) {
        authUserId = id;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}