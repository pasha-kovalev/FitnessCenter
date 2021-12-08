package com.epam.jwd.fitness_center.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order implements Entity{
    private static final long serialVersionUID = 8082141217268710611L;

    private Long id;
    private Long userDetailsId;
    private OrderStatus orderStatus;
    private Long itemId;
    private Long assignmentTrainerId;
    private Long trainerId;
    private BigDecimal price;
    private String comment;
    private final LocalDateTime creationDate;
    private String review;

    public Order(Builder builder) {
        this.id = builder.id;
        this.userDetailsId = builder.userDetailsId;
        this.orderStatus = builder.orderStatus;
        this.itemId = builder.itemId;
        this.assignmentTrainerId = builder.assignmentTrainerId;
        this.trainerId = builder.trainerId;
        this.price = builder.price;
        this.comment = builder.comment;
        this.creationDate = builder.creationDate;
        this.review = builder.review;
    }

    public Long getItemId() {
        return itemId;
    }

    public Order setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserDetailsId() {
        return userDetailsId;
    }

    public Order setUserDetailsId(Long userDetailsId) {
        this.userDetailsId = userDetailsId;
        return this;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public Long getAssignmentTrainerId() {
        return assignmentTrainerId;
    }

    public Order setAssignmentTrainerId(Long assignmentTrainerId) {
        this.assignmentTrainerId = assignmentTrainerId;
        return this;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public Order setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Order setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Order setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getReview() {
        return review;
    }

    public Order setReview(String review) {
        this.review = review;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", userDetailsId=").append(userDetailsId);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", itemId=").append(itemId);
        sb.append(", assignmentTrainerId=").append(assignmentTrainerId);
        sb.append(", trainerId=").append(trainerId);
        sb.append(", price=").append(price);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", review='").append(review).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {
        private Long id;
        private Long userDetailsId;
        private OrderStatus orderStatus;
        private Long itemId;
        private Long assignmentTrainerId;
        private Long trainerId;
        private BigDecimal price;
        private String comment;
        private LocalDateTime creationDate;
        private String review;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserDetailsId(Long userDetailsId) {
            this.userDetailsId = userDetailsId;
            return this;
        }

        public Builder setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setItemId(Long itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder setAssignmentTrainerId(Long assignmentTrainerId) {
            this.assignmentTrainerId = assignmentTrainerId;
            return this;
        }

        public Builder setTrainerId(Long trainerId) {
            this.trainerId = trainerId;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setCreationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder setReview(String review) {
            this.review = review;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
