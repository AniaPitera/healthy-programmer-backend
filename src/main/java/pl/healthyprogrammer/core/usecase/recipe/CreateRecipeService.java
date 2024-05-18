package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.healthyprogrammer.core.model.ingredient.Ingredient;
import pl.healthyprogrammer.core.model.ingredient.IngredientRepository;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.model.recipe_ingredient.RecipeIngredient;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeResponse;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientRequest;
import pl.healthyprogrammer.web.mapper.recipe.CreateRecipeMapper;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateRecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CreateRecipeMapper createRecipeMapper;

    @Transactional
    public CreateRecipeResponse createRecipe(CreateRecipeRequest request) {
        Recipe recipe = createRecipeMapper.mapToEntity(request);
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        for (RecipeIngredientRequest ingredientRequest : request.getRecipeIngredients()) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setQuantity(ingredientRequest.getQuantity());
            recipeIngredient.setUnit(ingredientRequest.getUnit());

            Ingredient ingredient = ingredientRepository.findById(ingredientRequest.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));

            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setRecipe(recipe);
            recipeIngredients.add(recipeIngredient);
        }

        recipe.setRecipeIngredients(recipeIngredients);
        Recipe savedRecipe = recipeRepository.save(recipe);

        return createRecipeMapper.mapToDto(savedRecipe);
    }
}
