package com.epam.jwd.fitness_center.model.entity;

import java.time.LocalDateTime;

public final class Token implements Entity {
    private static final long serialVersionUID = -4135987863303979517L;
    private final Long id;
    private final Long userId;
    private final String value;
    private final LocalDateTime creationDate;

    public Token(Long id, Long userId, String value, LocalDateTime creationDate) {
        this.id = id;
        this.userId = userId;
        this.value = value;
        this.creationDate = creationDate;
    }

    public Token(long userId, String value) {
        this(null, userId, value, null);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!id.equals(token.id)) return false;
        if (!userId.equals(token.userId)) return false;
        if (!value.equals(token.value)) return false;
        return creationDate.equals(token.creationDate);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + creationDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Token{");
        sb.append("id=").append(id);
        sb.append(", ownerId=").append(userId);
        sb.append(", value='").append(value).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append('}');
        return sb.toString();
    }
}
