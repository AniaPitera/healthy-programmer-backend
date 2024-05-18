package pl.healthyprogrammer.core.usecase.recipe;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.usecase.exception.RecipeNotFoundException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteRecipeService {
    private final RecipeRepository recipeRepository;

    @Transactional
    public void deleteRecipeById(UUID recipeId) {
        log.info("Deleting a recipe with ID: {}", recipeId);
        try {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
            recipeRepository.delete(recipe);
            log.info("Successfully deleted recipe with ID: {}", recipeId);
        } catch (RecipeNotFoundException e) {
            log.error("RecipeNotFoundException while deleting recipe with ID: {}. Error: {}", recipeId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting recipe with ID: {}. Error: {}", recipeId, e.getMessage(), e);
            throw e;
        }
    }
}
