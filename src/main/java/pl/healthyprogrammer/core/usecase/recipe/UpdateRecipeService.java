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
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

            updateRecipeMapper.updateRecipeUsingRequest(request, recipe);

            recipe.getRecipeIngredients().clear();

            for (UpdateRecipeIngredientRequest ingredientRequest : request.getRecipeIngredients()) {
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setQuantity(ingredientRequest.getQuantity());
                recipeIngredient.setUnit(ingredientRequest.getUnit());

                Ingredient ingredient = ingredientRepository.findById(ingredientRequest.getIngredientId())
                        .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found"));
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setRecipe(recipe);

                recipe.getRecipeIngredients().add(recipeIngredient);
            }
            Recipe updatedRecipe = recipeRepository.save(recipe);
            UpdateRecipeResponse response = updateRecipeMapper.mapToDto(updatedRecipe);
            log.info("Successfully updated recipe. Recipe ID: {}", recipeId);
            return response;
        } catch (RecipeNotFoundException e) {
            log.error("IngredientNotFoundException while updating a recipe: {}", e.getMessage());
            throw e;
        } catch (IngredientNotFoundException e) {
            log.error("RecipeNotFoundException while fetching a recipe with ID: {}. Error: {}", recipeId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during updating recipe with ID: {}. Error: {}", recipeId, e.getMessage(), e);
            throw e;
        }

    }
}
