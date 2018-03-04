package com.worldpay.exercise.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;


public class Offer implements Serializable{
    private String id;
    private String description;
    private BigDecimal price;
    private String currency;
    private int validityInSeconds;
    private LocalDateTime createdAt;
    private boolean valid;

    public Offer(final String id, final String description, final BigDecimal price, final String currency, final int validityInSeconds, final LocalDateTime createdAt, final boolean valid) {
        checkArgument(description != null, "Description is Mandatory");
        checkArgument(price != null && price.signum() > 0, "Price is Mandatory");
        checkArgument(currency != null, "Currency is Mandatory" );
        checkArgument(validityInSeconds > 0, "Validity period should be greater than 0");

        this.id = id;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.validityInSeconds = validityInSeconds;
        this.createdAt = createdAt;
        this.valid = valid;
    }


    public static Offer create(final String description, final BigDecimal price, final String currency, final int validityInSeconds) {
        String id = UUID.randomUUID().toString();
        LocalDateTime createdAt =  LocalDateTime.now();
        return new Offer(id, description, price, currency, validityInSeconds, createdAt, true);
    }

    public boolean shouldExpire() {
        LocalDateTime expiryTime = createdAt.plusSeconds(validityInSeconds);
        LocalDateTime currentTime = LocalDateTime.now();
        return !expiryTime.isAfter(currentTime);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public int getValidityInSeconds() {
        return validityInSeconds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isValid() {
        return valid;
    }

    public void cancel() {
        this.valid = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        return new EqualsBuilder()
                .append(validityInSeconds, offer.validityInSeconds)
                .append(valid, offer.valid)
                .append(id, offer.id)
                .append(description, offer.description)
                .append(price, offer.price)
                .append(currency, offer.currency)
                .append(createdAt, offer.createdAt)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(description)
                .append(price)
                .append(currency)
                .append(validityInSeconds)
                .append(createdAt)
                .append(valid)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("description", description)
                .append("price", price)
                .append("currency", currency)
                .append("validityInSeconds", validityInSeconds)
                .append("createdAt", createdAt)
                .append("valid", valid)
                .toString();
    }
}
