package pl.healthyprogrammer.core.usecase.recipe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.usecase.exception.IngredientNotFoundException;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeResponse;
import pl.healthyprogrammer.web.mapper.recipe.CreateRecipeMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRecipeService {
    private final RecipeRepository recipeRepository;
    private final CreateRecipeMapper createRecipeMapper;
    private final RecipeIngredientService recipeIngredientService;

    @Transactional
    public CreateRecipeResponse createRecipe(CreateRecipeRequest request) {
        log.info("Creating a recipe with title: {}", request.getTitle());
        try {
            Recipe recipe = createRecipeMapper.mapToEntity(request);
            recipeIngredientService.addRecipeIngredients(request.getRecipeIngredients(), recipe);
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
}