/*
package com.epam.jwd.fitness_center.model.entity;

public class Trainer extends User {
   */
/* //todo delete trainer add this fields to user
    // OR make inner class userMeta
    private String description;
    private String photoPath;

    public Trainer(Long id, String email, String password, String firstName, String secondName,
                   UserRole role, UserStatus status, String description, String photoPath) {
        super(id, email, password, firstName, secondName, role, status);
        this.description = description;
        this.photoPath = photoPath;
    }

    public Trainer(String email, String password, String firstName, String secondName,
                   UserRole role, UserStatus status, String description, String photoPath) {
        super(email, password, firstName, secondName, role, status);
        this.description = description;
        this.photoPath = photoPath;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Trainer trainer = (Trainer) o;

        if (description != null ? !description.equals(trainer.description) : trainer.description != null) return false;
        return photoPath != null ? photoPath.equals(trainer.photoPath) : trainer.photoPath == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (photoPath != null ? photoPath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Trainer{");
        sb.append(", id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", secondName='").append(secondName).append('\'');
        sb.append(", role=").append(role);
        sb.append(", status=").append(status);
        sb.append(", description='").append(description).append('\'');
        sb.append(", photoPath='").append(photoPath).append('\'');
        sb.append('}');
        return sb.toString();
    }*//*

}
*/
