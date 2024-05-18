package pl.healthyprogrammer.web.entrypoint.recipe;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.healthyprogrammer.core.usecase.recipe.*;
import pl.healthyprogrammer.web.dto.recipe.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipes")
public class RecipeController {
    private final CreateRecipeService createRecipeService;
    private final GetRecipeService getRecipeService;
    private final GetRecipesService getRecipesService;
    private final UpdateRecipeService updateRecipeService;
    private final DeleteRecipeService deleteRecipeService;

    @Operation(summary = "Create a new recipe")
    @PostMapping
    public ResponseEntity<CreateRecipeResponse> createRecipe(@Valid @RequestBody CreateRecipeRequest request) {
        var response = createRecipeService.createRecipe(request);
        return ResponseEntity.created(URI.create("/recipes/" + response.getId())).body(response);
    }

    @Operation(summary = "Show a page of recipes")
    @GetMapping
    public ResponseEntity<Page<RecipeResponse>> getRecipes(Pageable pageable) {
        return ResponseEntity.ok(getRecipesService.getRecipes(pageable));
    }

    @Operation(summary = "Show recipe details")
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable UUID recipeId) {
        return ResponseEntity.ok(getRecipeService.getRecipeById(recipeId));
    }

    @Operation(summary = "Update a recipe")
    @PutMapping("/{recipeId}")
    public ResponseEntity<UpdateRecipeResponse> updateRecipe(@PathVariable UUID recipeId, @Valid @RequestBody UpdateRecipeRequest request) {
        return ResponseEntity.ok(updateRecipeService.updateRecipe(recipeId, request));
    }

    @Operation(summary = "Delete a recipe")
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable UUID recipeId) {
        deleteRecipeService.deleteRecipeById(recipeId);
        return ResponseEntity.noContent().build();
    }
}
