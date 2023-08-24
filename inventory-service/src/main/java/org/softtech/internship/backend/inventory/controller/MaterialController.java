package org.softtech.internship.backend.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialCreateDTO;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialUpdateDTO;
import org.softtech.internship.backend.inventory.service.material.MaterialService;
import org.softtech.internship.backend.inventory.model.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/inventory/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping
    public ResponseEntity<? extends APIResponse<?>> read(@RequestParam(name = "id", required = false) String id,
                                                         @RequestParam(name = "name", required = false) String name) {
        if (id != null && !id.isEmpty()) {
            return materialService.getMaterialById(id);
        } else if (name != null && !name.isEmpty()) {
            return materialService.getMaterialByName(name);
        } else {
            return materialService.getAllMaterials();
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity<? extends APIResponse<?>> create(@RequestBody MaterialCreateDTO createDTO) {
        return materialService.createMaterial(createDTO);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<? extends APIResponse<?>> delete(@RequestParam(name = "id", required = false) String id,
                                                           @RequestParam(name = "name", required = false) String name) {
        if (id != null && !id.isEmpty()) {
            return materialService.deleteMaterial(id);
        } else if (name != null && !name.isEmpty()) {
            return materialService.deleteMaterial(name);
        } else {
            APIResponse<?> body = APIResponse.error("`id` or `name` must be given as request parameter");
            return ResponseEntity.unprocessableEntity().body(body);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<? extends APIResponse<?>> update(@PathVariable String id,
                                                           @RequestBody MaterialUpdateDTO updateDTO) {
        return materialService.update(id, updateDTO);
    }
}
