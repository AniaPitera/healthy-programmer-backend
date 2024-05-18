package pl.healthyprogrammer.web.entrypoint.recipe;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.healthyprogrammer.core.usecase.recipe.CreateRecipeService;
import pl.healthyprogrammer.core.usecase.recipe.DeleteRecipeService;
import pl.healthyprogrammer.core.usecase.recipe.GetRecipeService;
import pl.healthyprogrammer.core.usecase.recipe.UpdateRecipeService;
import pl.healthyprogrammer.web.dto.recipe.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final CreateRecipeService createRecipeService;
    private final GetRecipeService getRecipeService;
    private final UpdateRecipeService updateRecipeService;
    private final DeleteRecipeService deleteRecipeService;

    @PostMapping
    public ResponseEntity<CreateRecipeResponse> createRecipe(@Valid @RequestBody CreateRecipeRequest request) {
        var response = createRecipeService.createRecipe(request);
        return ResponseEntity.created(URI.create("/recipes/" + response.getId())).body(response);
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
