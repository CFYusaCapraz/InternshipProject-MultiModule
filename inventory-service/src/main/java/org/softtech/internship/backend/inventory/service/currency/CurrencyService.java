package org.softtech.internship.backend.inventory.service.currency;

import org.softtech.internship.backend.inventory.model.APIResponse;
import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyCreateDTO;
import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyUpdateDTO;
import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyViewDTO;
import org.softtech.internship.backend.inventory.model.currency.dto.LiveCurrencyDTO;
import org.softtech.internship.backend.inventory.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${external.api.url}")
    private String apiUrl;
    @Value("${external.api.apikey}")
    private String apiKey;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public ResponseEntity<? extends APIResponse<?>> getAllCurrencies() {
        try {
            List<CurrencyViewDTO> currencyList = new ArrayList<>();
            for (Currency currency : currencyRepository.findAllByIsDeletedIsFalse()) {
                currencyList.add(CurrencyMapper.viewMapper(currency));
            }
            if (!currencyList.isEmpty()) {
                APIResponse<List<CurrencyViewDTO>> body = APIResponse.successWithData(currencyList, "All currency information.");
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error("There are no currencies in the database.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting all the currencies!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> getCurrencyById(String id) {
        try {
            Optional<Currency> currency = currencyRepository.findCurrencyByCurrencyIdOrCurrencyNameAndIsDeletedIsFalse(UUID.fromString(id), null);
            if (currency.isPresent()) {
                CurrencyViewDTO viewDTO = CurrencyMapper.viewMapper(currency.get());
                APIResponse<CurrencyViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Currency information of ID: %s", id));
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error(String.format("Currency information of ID:`%s` not found!", id));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting the currency!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> getCurrencyByName(String name) {
        try {
            Optional<Currency> currency = currencyRepository.findCurrencyByCurrencyIdOrCurrencyNameAndIsDeletedIsFalse(null, name);
            if (currency.isPresent()) {
                CurrencyViewDTO viewDTO = CurrencyMapper.viewMapper(currency.get());
                APIResponse<CurrencyViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Currency information of Name: %s", name));
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error(String.format("Currency information of Name:`%s` not found!", name));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting the currency!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> createCurrency(CurrencyCreateDTO createDTO) {
        if (createDTO.getCurrency_name() == null || createDTO.getCurrency_name().isEmpty() || createDTO.getCurrency_rate() == null) {
            APIResponse<?> body = APIResponse.error("`currency_name` and `price` cannot be empty!");
            return ResponseEntity.unprocessableEntity().body(body);
        } else {
            Currency newCurrency = CurrencyMapper.createMapper(createDTO);
            try {
                currencyRepository.saveAndFlush(newCurrency);
                CurrencyViewDTO viewDTO = CurrencyMapper.viewMapper(newCurrency);
                APIResponse<CurrencyViewDTO> body = APIResponse.successWithData(viewDTO, "Currency has successfully created.");
                return ResponseEntity.status(HttpStatus.CREATED).body(body);
            } catch (DataIntegrityViolationException e) {
                APIResponse<?> body = APIResponse.error("Currency already exists!");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
            } catch (Exception e) {
                APIResponse<?> body = APIResponse.error("Error occurred while creating the currency!");
                return ResponseEntity.internalServerError().body(body);
            }
        }
    }

    public ResponseEntity<? extends APIResponse<?>> deleteCurrency(String field) {
        try {
            if (field == null || field.isEmpty()) {
                APIResponse<?> body = APIResponse.error("`id` or `name` cannot be empty!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                try {
                    UUID id = UUID.fromString(field);
                    Optional<Currency> currency = currencyRepository.findById(id);
                    if (currency.isPresent()) {
                        if (!currency.get().getIsDeleted()) {
                            Currency newCurrency = currency.get();
                            newCurrency.setIsDeleted(true);
                            currencyRepository.saveAndFlush(newCurrency);
                            APIResponse<?> body = APIResponse.success(String.format("Currency of ID: `%s` is successfully deleted.", field));
                            return ResponseEntity.ok(body);
                        } else {
                            APIResponse<?> body = APIResponse.success(String.format("Currency of ID: `%s` is already deleted!", field));
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                    } else {
                        APIResponse<?> body = APIResponse.success(String.format("Currency of ID: `%s` not found!", field));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                } catch (IllegalArgumentException e) {
                    Optional<Currency> currency = currencyRepository.findCurrencyByCurrencyName(field);
                    if (currency.isPresent()) {
                        if (!currency.get().getIsDeleted()) {
                            Currency newCurrency = currency.get();
                            newCurrency.setIsDeleted(true);
                            currencyRepository.saveAndFlush(newCurrency);
                            APIResponse<?> body = APIResponse.success(String.format("Currency of Name: `%s` is successfully deleted.", field));
                            return ResponseEntity.ok(body);
                        } else {
                            APIResponse<?> body = APIResponse.success(String.format("Currency of Name: `%s` is already deleted!", field));
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                    } else {
                        APIResponse<?> body = APIResponse.success(String.format("Currency of Name: `%s` not found!", field));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                }
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while deleting the currency!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> updateCurrency(String id, CurrencyUpdateDTO updateDTO) {
        try {
            if (id == null || id.isEmpty()) {
                APIResponse<?> body = APIResponse.error("Provide an ID to update the currency!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                if (updateDTO.isEmpty()) {
                    APIResponse<?> body = APIResponse.error("`currency_name` and `price` cannot be empty!");
                    return ResponseEntity.unprocessableEntity().body(body);
                } else {
                    Optional<Currency> currency = currencyRepository.findCurrencyByCurrencyIdOrCurrencyNameAndIsDeletedIsFalse(UUID.fromString(id), null);
                    if (currency.isPresent()) {
                        if (updateDTO.isSame(currency.get())) {
                            APIResponse<?> body = APIResponse.error("No update has been done!");
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                        Currency newCurrency = CurrencyMapper.updateMapper(currency.get(), updateDTO);
                        currencyRepository.saveAndFlush(newCurrency);
                        CurrencyViewDTO viewDTO = CurrencyMapper.viewMapper(newCurrency);
                        APIResponse<CurrencyViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Currency information of ID: `%s` is updated.", id));
                        return ResponseEntity.ok(body);
                    } else {
                        APIResponse<?> body = APIResponse.error(String.format("Currency information of ID: `%s` not found.", id));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                }
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while updating the currency!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> refresh() {
        try {
            if (updateLiveCurrencyData()) {
                APIResponse<?> body = APIResponse.success("Successfully refreshed currencies");
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error("Could not refresh successfully!");
                return ResponseEntity.internalServerError().body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while refreshing currencies!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public LiveCurrencyDTO fetchLiveCurrencyData() {
        String currencies = currencyRepository.findAll()
                .stream()
                .map(Currency::getCurrencyName)
                .collect(Collectors.joining(","));

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        String requestUrl = apiUrl + "?source=TRY&currencies=" + currencies;
        ResponseEntity<LiveCurrencyDTO> response = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, LiveCurrencyDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch live currency data");
        }
    }

    public boolean updateLiveCurrencyData() {
        try {
            LiveCurrencyDTO fetched = fetchLiveCurrencyData();
            LocalDateTime currentTimeMinusOneHour = LocalDateTime.now().minusSeconds(5);

            fetched.getQuotes().forEach((k, v) -> {
                String name = k.substring(3);
                BigDecimal value = BigDecimal.ONE.divide(v, MathContext.DECIMAL64);
                Currency currency = currencyRepository.findCurrencyByCurrencyName(name).orElseThrow();
                // if the last update time is not later than 1 hour, do not update the currency rate.
                if (currency.getUpdateTime().isBefore(currentTimeMinusOneHour)) {
                    currency.setCurrencyRate(value);
                    currencyRepository.saveAndFlush(currency);
                }
            });
            return true;
        } catch (Exception E) {
            return false;
        }
    }

}
