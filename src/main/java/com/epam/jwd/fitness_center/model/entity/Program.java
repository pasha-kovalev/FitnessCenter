package com.epam.jwd.fitness_center.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Program implements Entity {
    private static final long serialVersionUID = 2161643738559409089L;

    private Order order;
    private ProgramStatus programStatus;
    private String intensity;
    private String schedule;
    private String exercises;
    private String diet;
    private String equipment;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private UserRole roleUpdatedBy;
    private LocalDateTime updatedAt;

    public Program(Builder builder) {
        this.order = builder.order;
        this.programStatus = builder.programStatus;
        this.intensity = builder.intensity;
        this.schedule = builder.schedule;
        this.exercises = builder.exercises;
        this.diet = builder.diet;
        this.equipment = builder.equipment;
        this.startsAt = builder.startsAt;
        this.endsAt = builder.endsAt;
        this.roleUpdatedBy = builder.roleUpdatedBy;
        this.updatedAt = builder.updatedAt;
    }


    public Order getOrder() {
        return order;
    }

    public Program setOrder(Order order) {
        this.order = order;
        return this;
    }

    public ProgramStatus getProgramStatus() {
        return programStatus;
    }

    public Program setProgramStatus(ProgramStatus programStatus) {
        this.programStatus = programStatus;
        return this;
    }

    public String getIntensity() {
        return intensity;
    }

    public Program setIntensity(String intensity) {
        this.intensity = intensity;
        return this;
    }

    public String getSchedule() {
        return schedule;
    }

    public Program setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public String getExercises() {
        return exercises;
    }

    public Program setExercises(String exercises) {
        this.exercises = exercises;
        return this;
    }

    public String getDiet() {
        return diet;
    }

    public Program setDiet(String diet) {
        this.diet = diet;
        return this;
    }

    public String getEquipment() {
        return equipment;
    }

    public Program setEquipment(String equipment) {
        this.equipment = equipment;
        return this;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public Program setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public Program setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    public UserRole getRoleUpdatedBy() {
        return roleUpdatedBy;
    }

    public Program setRoleUpdatedBy(UserRole roleUpdatedBy) {
        this.roleUpdatedBy = roleUpdatedBy;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Program setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return order.equals(program.order) && programStatus == program.programStatus && Objects.equals(intensity, program.intensity) && Objects.equals(schedule, program.schedule) && Objects.equals(exercises, program.exercises) && Objects.equals(diet, program.diet) && Objects.equals(equipment, program.equipment) && Objects.equals(startsAt, program.startsAt) && Objects.equals(endsAt, program.endsAt) && roleUpdatedBy == program.roleUpdatedBy && Objects.equals(updatedAt, program.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, programStatus, intensity, schedule, exercises, diet, equipment, startsAt, endsAt, roleUpdatedBy, updatedAt);
    }

    public static class Builder {
        private Order order;
        private ProgramStatus programStatus;
        private String intensity;
        private String schedule;
        private String exercises;
        private String diet;
        private String equipment;
        private LocalDateTime startsAt;
        private LocalDateTime endsAt;
        private UserRole roleUpdatedBy;
        private LocalDateTime updatedAt;

        public Builder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public Builder setProgramStatus(ProgramStatus programStatus) {
            this.programStatus = programStatus;
            return this;
        }

        public Builder setIntensity(String intensity) {
            this.intensity = intensity;
            return this;
        }

        public Builder setSchedule(String schedule) {
            this.schedule = schedule;
            return this;
        }

        public Builder setExercises(String exercises) {
            this.exercises = exercises;
            return this;
        }

        public Builder setDiet(String diet) {
            this.diet = diet;
            return this;
        }

        public Builder setEquipment(String equipment) {
            this.equipment = equipment;
            return this;
        }

        public Builder setStartsAt(LocalDateTime startsAt) {
            this.startsAt = startsAt;
            return this;
        }

        public Builder setEndsAt(LocalDateTime endsAt) {
            this.endsAt = endsAt;
            return this;
        }

        public Builder setRoleUpdatedBy(UserRole roleUpdatedBy) {
            this.roleUpdatedBy = roleUpdatedBy;
            return this;
        }

        public Builder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Program build() {
            return new Program(this);
        }
    }
}
