package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;
import pl.healthyprogrammer.web.mapper.recipe.RecipeMapper;

@Service
@RequiredArgsConstructor
public class GetRecipesService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public Page<RecipeResponse> getRecipes(Pageable pageable) {
        Page<Recipe> result = recipeRepository.findAllWithIngredients(pageable);
        return recipeMapper.mapToDto(result);
    }
}
