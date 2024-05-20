package pl.healthyprogrammer.core.usecase.recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.usecase.exception.IngredientNotFoundException;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeResponse;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientRequest;
import pl.healthyprogrammer.web.mapper.recipe.CreateRecipeMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private CreateRecipeMapper createRecipeMapper;
    @Mock
    private RecipeIngredientService recipeIngredientService;


    @InjectMocks
    private CreateRecipeService createRecipeService;

    private CreateRecipeRequest request;
    private CreateRecipeResponse response;
    private Recipe recipe;
    private Recipe savedRecipe;

    @BeforeEach
    void setUp() {
        UUID ingredientId = UUID.randomUUID();
        RecipeIngredientRequest recipeIngredientRequest = new RecipeIngredientRequest(2.0, "cups", ingredientId);
        Set<RecipeIngredientRequest> recipeIngredientRequests = new HashSet<>();
        recipeIngredientRequests.add(recipeIngredientRequest);

        request = new CreateRecipeRequest("Delicious Pancakes", "Fluffy and delicious pancakes", "Mix and cook", recipeIngredientRequests);

        recipe = new Recipe();
        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());

        savedRecipe = new Recipe();
        savedRecipe.setTitle(request.getTitle());
        savedRecipe.setDescription(request.getDescription());
        savedRecipe.setInstructions(request.getInstructions());
        savedRecipe.setRecipeIngredients(new HashSet<>());

        response = new CreateRecipeResponse(UUID.randomUUID(), request.getTitle(), request.getDescription(), request.getInstructions(), new HashSet<>());
    }

    @Test
    void shouldCreateRecipe() {
        //given
        when(createRecipeMapper.mapToEntity(request)).thenReturn(recipe);
        doNothing().when(recipeIngredientService).addRecipeIngredients(request.getRecipeIngredients(), recipe);
        when(recipeRepository.save(recipe)).thenReturn(savedRecipe);
        when(createRecipeMapper.mapToDto(savedRecipe)).thenReturn(response);

        //when
        CreateRecipeResponse actualResponse = createRecipeService.createRecipe(request);

        //then
        assertNotNull(actualResponse);
        assertEquals(response, actualResponse);
        assertEquals(request.getTitle(), response.getTitle());

        verify(createRecipeMapper, times(1)).mapToEntity(request);
        verify(recipeIngredientService, times(1)).addRecipeIngredients(request.getRecipeIngredients(), recipe);
        verify(recipeRepository, times(1)).save(recipe);
        verify(createRecipeMapper, times(1)).mapToDto(savedRecipe);
    }

    @Test
    void shouldThrowIngredientNotFoundException() {
        // given
        when(createRecipeMapper.mapToEntity(request)).thenReturn(recipe);
        doThrow(new IngredientNotFoundException("Ingredient not found with id:"))
                .when(recipeIngredientService)
                .addRecipeIngredients(request.getRecipeIngredients(), recipe);

        // when & then
        assertThrows(
                IngredientNotFoundException.class,
                () -> createRecipeService.createRecipe(request));

        verify(createRecipeMapper, times(1)).mapToEntity(request);
        verify(recipeIngredientService, times(1)).addRecipeIngredients(request.getRecipeIngredients(), recipe);
        verify(recipeRepository, never()).save(any());
        verify(createRecipeMapper, never()).mapToDto(any());
    }

    @Test
    void shouldThrowUnexpectedException() {
        // given
        when(createRecipeMapper.mapToEntity(request)).thenThrow(new RuntimeException("Unexpected error"));

        // when & then
        assertThrows(
                RuntimeException.class,
                () -> createRecipeService.createRecipe(request));

        verify(createRecipeMapper, times(1)).mapToEntity(request);
        verify(recipeIngredientService, never()).addRecipeIngredients(anySet(), any());
        verify(recipeRepository, never()).save(any());
        verify(createRecipeMapper, never()).mapToDto(any());
    }

    @Test
    void shouldThrowExceptionWhenSavingRecipeFails() {
        // given
        when(createRecipeMapper.mapToEntity(request)).thenReturn(recipe);
        doNothing().when(recipeIngredientService).addRecipeIngredients(anySet(), any(Recipe.class));
        when(recipeRepository.save(recipe)).thenThrow(new RuntimeException("Database error"));

        // when & then
        assertThrows(
                RuntimeException.class,
                () -> createRecipeService.createRecipe(request));

        verify(createRecipeMapper, times(1)).mapToEntity(request);
        verify(recipeIngredientService, times(1)).addRecipeIngredients(request.getRecipeIngredients(), recipe);
        verify(recipeRepository, times(1)).save(recipe);
        verify(createRecipeMapper, never()).mapToDto(any());
    }
}