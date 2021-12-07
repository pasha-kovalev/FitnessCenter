package com.epam.jwd.fitness_center.model.entity;

import java.math.BigDecimal;

public final class UserDetails implements Entity {
    private static final long serialVersionUID = 1384401522272546369L;
    private long userId;
    private long cardId;
    private BigDecimal discount;
    private Long personalTrainerId;

    public UserDetails(long userId, long cardId, BigDecimal discount, Long personalTrainerId) {
        this.userId = userId;
        this.cardId = cardId;
        this.discount = discount;
        this.personalTrainerId = personalTrainerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
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
        sb.append(", cardId=").append(cardId);
        sb.append(", discount=").append(discount);
        sb.append(", personalTrainerId=").append(personalTrainerId);
        sb.append('}');
        return sb.toString();
    }
}
