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
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeResponse;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientRequest;
import pl.healthyprogrammer.web.mapper.recipe.CreateRecipeMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final CreateRecipeMapper createRecipeMapper;

    @Transactional
    public CreateRecipeResponse createRecipe(CreateRecipeRequest request) {
        log.info("Creating a recipe with title: {}", request.getTitle());
        try {
            Recipe recipe = createRecipeMapper.mapToEntity(request);
            Set<RecipeIngredient> recipeIngredients = addRecipeIngredients(request, recipe);
            recipe.setRecipeIngredients(recipeIngredients);
            Recipe savedRecipe = recipeRepository.save(recipe);
            CreateRecipeResponse response = createRecipeMapper.mapToDto(savedRecipe);
            log.info("Successfully created recipe with ID: {}", response.getId());
            return response;
        } catch (IngredientNotFoundException e) {
            log.error("IngredientNotFoundException while creating a recipe: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during creating a recipe with title: {}. Error: {}", request.getTitle(), e.getMessage(), e);
            throw e;
        }
    }

    private Set<RecipeIngredient> addRecipeIngredients(CreateRecipeRequest request, Recipe recipe) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();
        for (RecipeIngredientRequest ingredientRequest : request.getRecipeIngredients()) {
            RecipeIngredient recipeIngredient = addRecipeIngredient(ingredientRequest, recipe);
            recipeIngredients.add(recipeIngredient);
        }
        return recipeIngredients;
    }

    private RecipeIngredient addRecipeIngredient(RecipeIngredientRequest ingredientRequest, Recipe recipe) {
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
