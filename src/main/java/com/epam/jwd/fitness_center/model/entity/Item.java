package com.epam.jwd.fitness_center.model.entity;

import java.math.BigDecimal;

public final class Item implements Entity, Cloneable {
    private static final long serialVersionUID = 8222824259526138611L;
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;

    public Item(Long id, String name, BigDecimal price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Item(String name, BigDecimal price, String description) {
        this(null, name, price, description);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Item clone() throws CloneNotSupportedException {
        return (Item) super.clone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
