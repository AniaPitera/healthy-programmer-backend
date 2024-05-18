package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;
import pl.healthyprogrammer.web.mapper.recipe.RecipeMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeResponse getRecipeById(UUID recipeId) {
        Recipe recipe = recipeRepository.findByIdWithIngredients(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        return recipeMapper.mapToDto(recipe);
    }
}
