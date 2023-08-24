package org.softtech.internship.backend.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeCreateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeUpdateDTO;
import org.softtech.internship.backend.inventory.service.recipe.RecipeService;
import org.softtech.internship.backend.inventory.model.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/inventory/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<? extends APIResponse<?>> read(@RequestParam(name = "id", required = false) String id,
                                                         @RequestParam(name = "name", required = false) String name) {
        if (id != null && !id.isEmpty()) {
            return recipeService.getRecipeById(id);
        } else if (name != null && !name.isEmpty()) {
            return recipeService.getRecipeByName(name);
        } else {
            return recipeService.getAllRecipes();
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity<? extends APIResponse<?>> create(@RequestBody RecipeCreateDTO createDTO) {
        return recipeService.createRecipe(createDTO);
    }

    @DeleteMapping(path ="/delete")
    public ResponseEntity<? extends APIResponse<?>> delete(@RequestParam(name = "id", required = false) String id,
                                                           @RequestParam(name = "name", required = false) String name) {
        if (id != null && !id.isEmpty()) {
            return recipeService.deleteRecipe(id);
        } else if (name != null && !name.isEmpty()) {
            return recipeService.deleteRecipe(name);
        } else {
            APIResponse<?> body = APIResponse.error("`id` or `name` must be given as request parameter");
            return ResponseEntity.unprocessableEntity().body(body);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<? extends APIResponse<?>> update(@PathVariable String id,
                                                           @RequestBody RecipeUpdateDTO updateDTO) {
        return recipeService.updateRecipe(id, updateDTO);
    }
}
