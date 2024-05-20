package pl.healthyprogrammer.core.usecase.recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.healthyprogrammer.core.model.ingredient.Ingredient;
import pl.healthyprogrammer.core.model.ingredient.IngredientRepository;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe_ingredient.RecipeIngredient;
import pl.healthyprogrammer.core.usecase.exception.IngredientNotFoundException;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientRequest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeIngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeIngredientService recipeIngredientService;

    private UUID ingredientId;
    private Recipe recipe;
    private Ingredient ingredient;
    private RecipeIngredientRequest ingredientRequest;
    private Set<RecipeIngredientRequest> ingredientRequests;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setRecipeIngredients(new HashSet<>());

        ingredientId = UUID.randomUUID();

        ingredient = new Ingredient();
        ingredient.setId(ingredientId);

        ingredientRequest = new RecipeIngredientRequest(2.0, "cups", ingredientId);

        ingredientRequests = new HashSet<>();
        ingredientRequests.add(ingredientRequest);
    }

    @Test
    void shouldClearRecipeIngredients() {
        // given
        recipe.getRecipeIngredients().add(new RecipeIngredient());

        // when
        recipeIngredientService.clearRecipeIngredients(recipe);

        // then
        assertTrue(recipe.getRecipeIngredients().isEmpty());
    }

    @Test
    void shouldAddRecipeIngredients() {
        // given

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));

        // when
        recipeIngredientService.addRecipeIngredients(ingredientRequests, recipe);

        // then
        assertEquals(1, recipe.getRecipeIngredients().size());
        RecipeIngredient recipeIngredient = recipe.getRecipeIngredients().iterator().next();
        assertEquals(ingredient, recipeIngredient.getIngredient());
        assertEquals(recipe, recipeIngredient.getRecipe());
        assertEquals(ingredientRequest.getQuantity(), recipeIngredient.getQuantity());
        assertEquals(ingredientRequest.getUnit(), recipeIngredient.getUnit());

        verify(ingredientRepository, times(1)).findById(ingredientId);
    }

    @Test
    void shouldThrowIngredientNotFoundExceptionWhenAddingRecipeIngredients() {
        // given
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(
                IngredientNotFoundException.class,
                () -> recipeIngredientService.addRecipeIngredients(ingredientRequests, recipe)
        );

        assertTrue(recipe.getRecipeIngredients().isEmpty());

        verify(ingredientRepository, times(1)).findById(ingredient.getId());
    }
}