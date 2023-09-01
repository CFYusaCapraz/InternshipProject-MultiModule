package org.softtech.internship.backend.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.inventory.model.APIResponse;
import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyCreateDTO;
import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyUpdateDTO;
import org.softtech.internship.backend.inventory.service.currency.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/inventory/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<? extends APIResponse<?>> read(@RequestParam(name = "id", required = false) String id,
                                                         @RequestParam(name = "name", required = false) String name) {
        if (id != null && !id.isEmpty()) {
            return currencyService.getCurrencyById(id);
        } else if (name != null && !name.isEmpty()) {
            return currencyService.getCurrencyByName(name);
        } else {
            return currencyService.getAllCurrencies();
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity<? extends APIResponse<?>> create(@RequestBody CurrencyCreateDTO createDTO) {
        return currencyService.createCurrency(createDTO);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<? extends APIResponse<?>> delete(@RequestParam(name = "id", required = false) String id,
                                                           @RequestParam(name = "name", required = false) String name) {
        if (id != null && !id.isEmpty()) {
            return currencyService.deleteCurrency(id);
        } else if (name != null && !name.isEmpty()) {
            return currencyService.deleteCurrency(name);
        } else {
            APIResponse<?> body = APIResponse.error("`id` or `name` must be given as request parameter");
            return ResponseEntity.unprocessableEntity().body(body);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<? extends APIResponse<?>> update(@PathVariable String id,
                                                           @RequestBody CurrencyUpdateDTO updateDTO) {
        return currencyService.updateCurrency(id, updateDTO);
    }

    @GetMapping(path = "/refresh")
    public ResponseEntity<? extends APIResponse<?>> refresh(){
        return currencyService.refresh();
    }
}
