package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.usecase.exception.RecipeNotFoundException;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;
import pl.healthyprogrammer.web.mapper.recipe.RecipeMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeResponse getRecipeById(UUID recipeId) {
        log.info("Attempting to fetch recipe with ID: {}", recipeId);
        try {
            Recipe recipe = recipeRepository.findByIdWithIngredients(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
            RecipeResponse response = recipeMapper.mapToDto(recipe);
            log.info("Successfully fetched recipe with ID: {}", recipeId);
            return response;
        } catch (RecipeNotFoundException e) {
            log.error("RecipeNotFoundException while fetching a recipe with ID: {}. Error: {}", recipeId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching recipe with ID: {}. Error: {}", recipeId, e.getMessage(), e);
            throw e;
        }
    }
}
