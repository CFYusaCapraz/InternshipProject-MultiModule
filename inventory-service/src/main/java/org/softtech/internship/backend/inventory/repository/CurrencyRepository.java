package org.softtech.internship.backend.inventory.repository;

import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
    Optional<Currency> findCurrencyByCurrencyName(String currencyName);

    List<Currency> findAllByIsDeletedIsFalse();

    Optional<Currency> findCurrencyByCurrencyIdOrCurrencyNameAndIsDeletedIsFalse(UUID currencyId, String currencyName);
}
