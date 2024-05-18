package pl.healthyprogrammer.web.entrypoint.recipe;

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
public class RecipeController {
    private final CreateRecipeService createRecipeService;
    private final GetRecipeService getRecipeService;
    private final GetRecipesService getRecipesService;
    private final UpdateRecipeService updateRecipeService;
    private final DeleteRecipeService deleteRecipeService;

    @PostMapping
    public ResponseEntity<CreateRecipeResponse> createRecipe(@Valid @RequestBody CreateRecipeRequest request) {
        var response = createRecipeService.createRecipe(request);
        return ResponseEntity.created(URI.create("/recipes/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<RecipeResponse>> getRecipes(Pageable pageable) {
        return ResponseEntity.ok(getRecipesService.getRecipes(pageable));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable UUID recipeId) {
        return ResponseEntity.ok(getRecipeService.getRecipeById(recipeId));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<UpdateRecipeResponse> updateRecipe(@PathVariable UUID recipeId, @Valid @RequestBody UpdateRecipeRequest request) {
        return ResponseEntity.ok(updateRecipeService.updateRecipe(recipeId, request));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable UUID recipeId) {
        deleteRecipeService.deleteRecipeById(recipeId);
        return ResponseEntity.noContent().build();
    }
}
