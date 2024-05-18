package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.healthyprogrammer.core.model.ingredient.Ingredient;
import pl.healthyprogrammer.core.model.ingredient.IngredientRepository;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.model.recipe_ingredient.RecipeIngredient;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeResponse;
import pl.healthyprogrammer.web.dto.recipe_ingredient.UpdateRecipeIngredientRequest;
import pl.healthyprogrammer.web.mapper.recipe.UpdateRecipeMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateRecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UpdateRecipeMapper updateRecipeMapper;

    @Transactional
    public UpdateRecipeResponse updateRecipe(UUID recipeId, UpdateRecipeRequest request) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        updateRecipeMapper.updateRecipeUsingRequest(request, recipe);

        recipe.getRecipeIngredients().clear();

        for (UpdateRecipeIngredientRequest ingredientRequest : request.getRecipeIngredients()) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setQuantity(ingredientRequest.getQuantity());
            recipeIngredient.setUnit(ingredientRequest.getUnit());

            Ingredient ingredient = ingredientRepository.findById(ingredientRequest.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setRecipe(recipe);

            recipe.getRecipeIngredients().add(recipeIngredient);
        }
        Recipe updatedRecipe = recipeRepository.save(recipe);

        return updateRecipeMapper.mapToDto(updatedRecipe);
    }
}
