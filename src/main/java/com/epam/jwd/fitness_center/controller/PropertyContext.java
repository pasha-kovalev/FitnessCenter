package com.epam.jwd.fitness_center.controller;

public interface PropertyContext {
    String get(String name);

    static PropertyContext getInstance() {
        return SimplePropertyContext.getInstance();
    }
}
