package pl.healthyprogrammer.core.usecase.recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.healthyprogrammer.core.model.ingredient.Ingredient;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.model.recipe_ingredient.RecipeIngredient;
import pl.healthyprogrammer.core.usecase.exception.IngredientNotFoundException;
import pl.healthyprogrammer.core.usecase.exception.RecipeNotFoundException;
import pl.healthyprogrammer.web.dto.ingredient.IngredientResponse;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeResponse;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientRequest;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientResponse;
import pl.healthyprogrammer.web.mapper.recipe.UpdateRecipeMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private UpdateRecipeMapper updateRecipeMapper;
    @Mock
    private RecipeIngredientService recipeIngredientService;

    @InjectMocks
    private UpdateRecipeService updateRecipeService;

    private UUID recipeId;
    private Recipe recipe, updatedRecipe;
    private UpdateRecipeRequest request;
    private UpdateRecipeResponse response;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        UUID newIngredientId = UUID.randomUUID();
        RecipeIngredientRequest recipeIngredientRequest = new RecipeIngredientRequest(1.5, "tablespoons", newIngredientId);
        Set<RecipeIngredientRequest> recipeIngredientRequests = new HashSet<>();
        recipeIngredientRequests.add(recipeIngredientRequest);

        request = new UpdateRecipeRequest("New title", "New description", "New instructions", recipeIngredientRequests);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName("Flour");
        ingredient.setCaloriesPerUnit(50.0);

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setQuantity(2.0);
        recipeIngredient.setUnit("cups");
        recipeIngredient.setIngredient(ingredient);

        Set<RecipeIngredient> recipeIngredients = new HashSet<>();
        recipeIngredients.add(recipeIngredient);

        recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setTitle("Delicious Pancakes");
        recipe.setDescription("Fluffy and delicious pancakes");
        recipe.setInstructions("Mix and cook");
        recipe.setRecipeIngredients(recipeIngredients);

        Ingredient newIngredient = new Ingredient();
        ingredient.setId(newIngredientId);
        ingredient.setName("Sugar");
        ingredient.setCaloriesPerUnit(100.0);

        RecipeIngredient newRecipeIngredient = new RecipeIngredient();
        newRecipeIngredient.setQuantity(1.5);
        newRecipeIngredient.setUnit("tablespoons");
        newRecipeIngredient.setIngredient(newIngredient);

        Set<RecipeIngredient> newRecipeIngredients = new HashSet<>();
        newRecipeIngredients.add(newRecipeIngredient);

        updatedRecipe = new Recipe();
        updatedRecipe.setId(recipeId);
        updatedRecipe.setTitle("New title");
        updatedRecipe.setDescription("New description");
        updatedRecipe.setInstructions("New instructions");
        updatedRecipe.setRecipeIngredients(newRecipeIngredients);

        RecipeIngredientResponse recipeIngredientResponse = new RecipeIngredientResponse(recipeId, 2.0, "cups", new IngredientResponse(newIngredientId, "Sugar", 100.0));

        Set<RecipeIngredientResponse> recipeIngredientResponses = new HashSet<>();
        recipeIngredientResponses.add(recipeIngredientResponse);

        response = new UpdateRecipeResponse(recipeId, "New title", "New description", "New instructions", recipeIngredientResponses);
    }

    @Test
    void shouldUpdateRecipe() {
        //given
        when(recipeRepository.findByIdWithIngredients(recipeId)).thenReturn(Optional.of(recipe));
        doNothing().when(updateRecipeMapper).updateRecipeUsingRequest(request, recipe);
        doNothing().when(recipeIngredientService).clearRecipeIngredients(recipe);
        doNothing().when(recipeIngredientService).addRecipeIngredients(request.getRecipeIngredients(), recipe);
        when(recipeRepository.save(recipe)).thenReturn(updatedRecipe);
        when(updateRecipeMapper.mapToDto(updatedRecipe)).thenReturn(response);

        //when
        UpdateRecipeResponse actualResponse = updateRecipeService.updateRecipe(recipeId, request);

        // then
        assertNotNull(actualResponse);
        assertEquals(response, actualResponse);

        verify(recipeRepository, times(1)).findByIdWithIngredients(recipeId);
        verify(updateRecipeMapper, times(1)).updateRecipeUsingRequest(request, recipe);
        verify(recipeIngredientService, times(1)).clearRecipeIngredients(recipe);
        verify(recipeIngredientService, times(1)).addRecipeIngredients(request.getRecipeIngredients(), recipe);
        verify(recipeRepository, times(1)).save(recipe);
        verify(updateRecipeMapper, times(1)).mapToDto(updatedRecipe);
    }

    @Test
    void shouldThrowRecipeNotFoundException() {
        //given
        when(recipeRepository.findByIdWithIngredients(recipeId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(
                RecipeNotFoundException.class,
                () -> updateRecipeService.updateRecipe(recipeId, request));

        verify(recipeRepository, times(1)).findByIdWithIngredients(recipeId);
        verify(updateRecipeMapper, never()).updateRecipeUsingRequest(any(), any());
        verify(recipeIngredientService, never()).clearRecipeIngredients(any());
        verify(recipeIngredientService, never()).addRecipeIngredients(anySet(), any());
        verify(recipeRepository, never()).save(any());
        verify(updateRecipeMapper, never()).mapToDto(any());
    }


    @Test
    void shouldThrowIngredientNotFoundException() {
        //given
        when(recipeRepository.findByIdWithIngredients(recipeId)).thenReturn(Optional.of(recipe));
        doThrow(new IngredientNotFoundException("Ingredient not found with id: ")).when(recipeIngredientService).addRecipeIngredients(request.getRecipeIngredients(), recipe);

        //when & then
        assertThrows(
                IngredientNotFoundException.class,
                () -> updateRecipeService.updateRecipe(recipeId, request));

        verify(recipeRepository, times(1)).findByIdWithIngredients(recipeId);
        verify(updateRecipeMapper, times(1)).updateRecipeUsingRequest(request, recipe);
        verify(recipeIngredientService, times(1)).clearRecipeIngredients(recipe);
        verify(recipeIngredientService, times(1)).addRecipeIngredients(request.getRecipeIngredients(), recipe);
        verify(recipeRepository, never()).save(any());
        verify(updateRecipeMapper, never()).mapToDto(any());

    }

    @Test
    void shouldThrowUnexpectedException() {
        //given
        when(recipeRepository.findByIdWithIngredients(recipeId)).thenReturn(Optional.of(recipe));
        doThrow(new RuntimeException("Unexpected error")).when(updateRecipeMapper).updateRecipeUsingRequest(request, recipe);

        //when & then
        assertThrows(
                RuntimeException.class,
                () -> updateRecipeService.updateRecipe(recipeId, request));

        verify(recipeRepository, times(1)).findByIdWithIngredients(recipeId);
        verify(updateRecipeMapper, times(1)).updateRecipeUsingRequest(request, recipe);
        verify(recipeIngredientService, never()).clearRecipeIngredients(any());
        verify(recipeIngredientService, never()).addRecipeIngredients(any(), any());
        verify(recipeRepository, never()).save(any());
        verify(updateRecipeMapper, never()).mapToDto(any());
    }
}