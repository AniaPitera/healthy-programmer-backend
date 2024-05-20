package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;
import pl.healthyprogrammer.web.mapper.recipe.RecipeMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRecipesService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public Page<RecipeResponse> getRecipes(Pageable pageable) {
        log.info("Fetching recipes for page: {} with size: {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Recipe> result = fetchRecipes(pageable);
            Page<RecipeResponse> response = recipeMapper.mapPageToDto(result);
            log.info("Successfully fetched recipes for page: {} with size: {}.", pageable.getPageNumber(), pageable.getPageSize());
            return response;
        } catch (Exception e) {
            log.error("Unexpected error occurred while recipes for page: {} with size: {}. Error: {}", pageable.getPageNumber(), pageable.getPageSize(), e.getMessage(), e);
            throw e;
        }
    }

    private Page<Recipe> fetchRecipes(Pageable pageable) {
        return recipeRepository.findAllWithIngredients(pageable);
    }
}
