package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.healthyprogrammer.core.model.ingredient.Ingredient;
import pl.healthyprogrammer.core.model.ingredient.IngredientRepository;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe_ingredient.RecipeIngredient;
import pl.healthyprogrammer.core.usecase.exception.IngredientNotFoundException;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientRequest;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {
    private final IngredientRepository ingredientRepository;

    public void clearRecipeIngredients(Recipe recipe) {
        recipe.getRecipeIngredients().clear();
    }

    public void addRecipeIngredients(Set<RecipeIngredientRequest> ingredientRequests, Recipe recipe) {
        for (RecipeIngredientRequest ingredientRequest : ingredientRequests) {
            RecipeIngredient recipeIngredient = createRecipeIngredient(ingredientRequest, recipe);
            recipe.getRecipeIngredients().add(recipeIngredient);
        }
    }

    private RecipeIngredient createRecipeIngredient(RecipeIngredientRequest ingredientRequest, Recipe recipe) {
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