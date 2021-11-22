package com.epam.jwd.fitness_center.controller;

public final class SimplePropertyContext implements PropertyContext{
    private SimplePropertyContext() {
    }

    @Override
    public String get(String name) {
        return PagePath.of(name).getPath();
    }

    static SimplePropertyContext getInstance() {
        return SimplePropertyContextHolder.INSTANCE;
    }

    private static class SimplePropertyContextHolder {
        private static final SimplePropertyContext INSTANCE = new SimplePropertyContext();
    }
}
