package com.epam.jwd.fitness_center.model.entity;

import java.math.BigDecimal;

public final class UserDetails implements Entity {
    private static final long serialVersionUID = 1384401522272546369L;
    private long userId;
    private BigDecimal discount;
    private Long personalTrainerId;

    public UserDetails(long userId, BigDecimal discount, Long personalTrainerId) {
        this.userId = userId;
        this.discount = discount;
        this.personalTrainerId = personalTrainerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Long getPersonalTrainerId() {
        return personalTrainerId;
    }

    public void setPersonalTrainerId(Long personalTrainerId) {
        this.personalTrainerId = personalTrainerId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDetails{");
        sb.append("userId=").append(userId);
        sb.append(", discount=").append(discount);
        sb.append(", personalTrainerId=").append(personalTrainerId);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetails that = (UserDetails) o;

        if (userId != that.userId) return false;
        if (discount != null ? !discount.equals(that.discount) : that.discount != null) return false;
        return personalTrainerId != null ? personalTrainerId.equals(that.personalTrainerId) : that.personalTrainerId == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        result = 31 * result + (personalTrainerId != null ? personalTrainerId.hashCode() : 0);
        return result;
    }
}
