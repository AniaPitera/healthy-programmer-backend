package pl.healthyprogrammer.core.usecase.recipe;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteRecipeService {
    private final RecipeRepository recipeRepository;

    @Transactional
    public void deleteRecipeById(UUID recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        recipeRepository.delete(recipe);
    }
}
