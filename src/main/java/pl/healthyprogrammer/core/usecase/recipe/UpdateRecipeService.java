package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.healthyprogrammer.core.model.ingredient.Ingredient;
import pl.healthyprogrammer.core.model.ingredient.IngredientRepository;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.model.recipe_ingredient.RecipeIngredient;
import pl.healthyprogrammer.core.usecase.exception.IngredientNotFoundException;
import pl.healthyprogrammer.core.usecase.exception.RecipeNotFoundException;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeResponse;
import pl.healthyprogrammer.web.dto.recipe_ingredient.UpdateRecipeIngredientRequest;
import pl.healthyprogrammer.web.mapper.recipe.UpdateRecipeMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateRecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UpdateRecipeMapper updateRecipeMapper;

    @Transactional
    public UpdateRecipeResponse updateRecipe(UUID recipeId, UpdateRecipeRequest request) {
        log.info("Updating recipe with ID: {}", recipeId);
        try {
            Recipe recipe = fetchRecipe(recipeId);
            updateRecipeMapper.updateRecipeUsingRequest(request, recipe);
            clearRecipeIngredients(recipe);
            updateRecipeIngredients(request, recipe);
            Recipe updatedRecipe = recipeRepository.save(recipe);
            UpdateRecipeResponse response = updateRecipeMapper.mapToDto(updatedRecipe);
            log.info("Successfully updated recipe. Recipe ID: {}", recipeId);
            return response;
        } catch (RecipeNotFoundException e) {
            log.error("RecipeNotFoundException while updating a recipe: {}", e.getMessage());
            throw e;
        } catch (IngredientNotFoundException e) {
            log.error("IngredientNotFoundException while fetching a recipe with ID: {}. Error: {}", recipeId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during updating recipe with ID: {}. Error: {}", recipeId, e.getMessage(), e);
            throw e;
        }
    }

    private Recipe fetchRecipe(UUID recipeId) {
        return recipeRepository.findByIdWithIngredients(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
    }

    private void clearRecipeIngredients(Recipe recipe) {
        recipe.getRecipeIngredients().clear();
    }

    private void updateRecipeIngredients(UpdateRecipeRequest request, Recipe recipe) {
        for (UpdateRecipeIngredientRequest ingredientRequest : request.getRecipeIngredients()) {
            RecipeIngredient recipeIngredient = addRecipeIngredient(ingredientRequest, recipe);
            recipe.getRecipeIngredients().add(recipeIngredient);
        }
    }

    private RecipeIngredient addRecipeIngredient(UpdateRecipeIngredientRequest ingredientRequest, Recipe recipe) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setQuantity(ingredientRequest.getQuantity());
        recipeIngredient.setUnit(ingredientRequest.getUnit());

        Ingredient ingredient = fetchIngredient(ingredientRequest.getIngredientId());
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setRecipe(recipe);

        return recipeIngredient;
    }

    private Ingredient fetchIngredient(UUID ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found with id: " + ingredientId));
    }
}
