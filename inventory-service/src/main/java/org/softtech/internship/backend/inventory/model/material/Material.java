package org.softtech.internship.backend.inventory.model.material;

import jakarta.persistence.*;
import lombok.*;
import org.softtech.internship.backend.inventory.model.currency.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "materials")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID materialId;
    @Column(nullable = false, unique = true)
    private String materialName;
    @Column(nullable = false)
    private BigDecimal unitPrice;
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
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
        Material material = (Material) o;
        return Objects.equals(materialId, material.materialId) && Objects.equals(materialName, material.materialName) && Objects.equals(unitPrice, material.unitPrice) && Objects.equals(currency, material.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, materialName, unitPrice, currency);
    }

    @Override
    public String toString() {
        return "Material{" + "materialId=" + materialId + ", materialName='" + materialName + '\'' + ", unitPrice=" + unitPrice + ", currency=" + currency + '}';
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime time = now();
        creationTime = time;
        updateTime = time;
        isDeleted = false;
    }

    // TODO: Learn more about @UpdateTimeStamp annotations
    @PreUpdate
    public void onUpdate() {
        updateTime = now();
    }
}
