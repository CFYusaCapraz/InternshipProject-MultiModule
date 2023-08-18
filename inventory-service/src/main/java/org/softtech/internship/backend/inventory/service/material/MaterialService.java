package org.softtech.internship.backend.inventory.service.material;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialCreateDTO;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialUpdateDTO;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialViewDTO;
import org.softtech.internship.backend.inventory.model.APIResponse;
import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.repository.CurrencyRepository;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final CurrencyRepository currencyRepository;

    public ResponseEntity<? extends APIResponse<?>> getAllMaterials() {
        try {
            List<MaterialViewDTO> materialList = new ArrayList<>();
            for (Material material : materialRepository.findAllByIsDeletedIsFalse()) {
                materialList.add(MaterialMapper.viewMapper(material));
            }
            if (!materialList.isEmpty()) {
                APIResponse<List<MaterialViewDTO>> body = APIResponse.successWithData(materialList, "All material information.");
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error("There are no materials in the database.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting all the materials!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> getMaterialById(String id) {
        try {
            Optional<Material> material = materialRepository.findMaterialByMaterialIdOrMaterialNameAndIsDeletedIsFalse(UUID.fromString(id), null);
            if (material.isPresent()) {
                MaterialViewDTO viewDTO = MaterialMapper.viewMapper(material.get());
                APIResponse<MaterialViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Material information of ID: %s", id));
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error(String.format("Material information of ID:`%s` not found!", id));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting the material!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> getMaterialByName(String name) {
        try {
            Optional<Material> material = materialRepository.findMaterialByMaterialIdOrMaterialNameAndIsDeletedIsFalse(null, name);
            if (material.isPresent()) {
                MaterialViewDTO viewDTO = MaterialMapper.viewMapper(material.get());
                APIResponse<MaterialViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Material information of Name: %s", name));
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error(String.format("Material information of Name:`%s` not found!", name));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting the material!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> createMaterial(MaterialCreateDTO createDTO) {
        if ((createDTO.getMaterial_name() == null || createDTO.getMaterial_name().isEmpty())
                && (createDTO.getCurrency_name() == null || createDTO.getCurrency_name().isEmpty())
                && createDTO.getPrice() == null) {
            APIResponse<?> body = APIResponse.error("`material_name`, `price` and `currency_name` cannot be empty!");
            return ResponseEntity.unprocessableEntity().body(body);
        } else {
            try {
                Optional<Currency> currency = currencyRepository.findCurrencyByCurrencyName(createDTO.getCurrency_name());
                if (currency.isPresent()) {
                    Material newMaterial = MaterialMapper.createMapper(createDTO, currency.get());
                    materialRepository.saveAndFlush(newMaterial);
                    MaterialViewDTO viewDTO = MaterialMapper.viewMapper(newMaterial);
                    APIResponse<MaterialViewDTO> body = APIResponse.successWithData(viewDTO, "Material has successfully created.");
                    return ResponseEntity.status(HttpStatus.CREATED).body(body);
                } else {
                    APIResponse<MaterialViewDTO> body = APIResponse.error("Given currency not found!");
                    return ResponseEntity.status(HttpStatus.CREATED).body(body);
                }
            } catch (DataIntegrityViolationException e) {
                APIResponse<?> body = APIResponse.error("Material already exists!");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
            } catch (Exception e) {
                APIResponse<?> body = APIResponse.error("Error occurred while creating the material!");
                return ResponseEntity.internalServerError().body(body);
            }
        }
    }

    public ResponseEntity<? extends APIResponse<?>> deleteMaterial(String field) {
        try {
            if (field == null || field.isEmpty()) {
                APIResponse<?> body = APIResponse.error("`id` or `name` cannot be empty!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                try {
                    UUID id = UUID.fromString(field);
                    Optional<Material> material = materialRepository.findById(id);
                    if (material.isPresent()) {
                        if (!material.get().getIsDeleted()) {
                            Material newMaterial = material.get();
                            newMaterial.setIsDeleted(true);
                            materialRepository.saveAndFlush(newMaterial);
                            APIResponse<?> body = APIResponse.success(String.format("Material of ID: `%s` is successfully deleted.", field));
                            return ResponseEntity.ok(body);
                        } else {
                            APIResponse<?> body = APIResponse.success(String.format("Material of ID: `%s` is already deleted!", field));
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                    } else {
                        APIResponse<?> body = APIResponse.success(String.format("Material of ID: `%s` not found!", field));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                } catch (IllegalArgumentException e) {
                    Optional<Material> material = materialRepository.findMaterialByMaterialName(field);
                    if (material.isPresent()) {
                        if (!material.get().getIsDeleted()) {
                            Material newMaterial = material.get();
                            newMaterial.setIsDeleted(true);
                            materialRepository.saveAndFlush(newMaterial);
                            APIResponse<?> body = APIResponse.success(String.format("Material of Name: `%s` is successfully deleted.", field));
                            return ResponseEntity.ok(body);
                        } else {
                            APIResponse<?> body = APIResponse.success(String.format("Material of Name: `%s` is already deleted!", field));
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                    } else {
                        APIResponse<?> body = APIResponse.success(String.format("Material of Name: `%s` not found!", field));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                }
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while deleting the material!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> update(String id, MaterialUpdateDTO updateDTO) {
        try {
            if (id == null || id.isEmpty()) {
                APIResponse<?> body = APIResponse.error("Provide an ID to update the material!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                if (updateDTO.isEmpty()) {
                    APIResponse<?> body = APIResponse.error("`material_name`, `price` and `currency_name` cannot be empty!");
                    return ResponseEntity.unprocessableEntity().body(body);
                } else {
                    Optional<Currency> currency = currencyRepository.findCurrencyByCurrencyName(updateDTO.getCurrency_name());
                    if (currency.isEmpty()) {
                        APIResponse<?> body = APIResponse.error("Given currency not found");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                    Optional<Material> material = materialRepository.findMaterialByMaterialIdOrMaterialNameAndIsDeletedIsFalse(UUID.fromString(id), null);
                    if (material.isPresent()) {
                        if (updateDTO.isSame(material.get())) {
                            APIResponse<?> body = APIResponse.error("No update has been done!");
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                        Material newMaterial = MaterialMapper.updateMapper(material.get(), updateDTO, currency.get());
                        materialRepository.saveAndFlush(newMaterial);
                        MaterialViewDTO viewDTO = MaterialMapper.viewMapper(newMaterial);
                        APIResponse<MaterialViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Material information of ID: `%s` is updated.", id));
                        return ResponseEntity.ok(body);
                    } else {
                        APIResponse<?> body = APIResponse.error(String.format("Material information of ID: `%s` not found.", id));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                }
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while updating the material!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public List<Material> createMaterialList(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Material> materialList = new ArrayList<>();
            for (String x : stringList) {
                Optional<Material> material = materialRepository.findById(UUID.fromString(x));
                material.ifPresent(materialList::add);
            }
            return materialList;
        }
    }
}
