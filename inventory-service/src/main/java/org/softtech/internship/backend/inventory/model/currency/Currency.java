package org.softtech.internship.backend.inventory.model.currency;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "currencies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID currencyId;
    @Column(nullable = false, unique = true)
    private String currencyName;
    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal currencyRate;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateTime;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(currencyId, currency.currencyId) && Objects.equals(currencyName, currency.currencyName) && Objects.equals(currencyRate, currency.currencyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyId, currencyName, currencyRate);
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime time = now();
        creationTime = time;
        updateTime = time;
        isDeleted = false;
    }

    @PreUpdate
    public void onUpdate() {
        updateTime = now();
    }
}
