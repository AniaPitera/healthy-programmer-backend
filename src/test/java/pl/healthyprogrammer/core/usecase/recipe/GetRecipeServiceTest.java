package pl.healthyprogrammer.core.usecase.recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.core.usecase.exception.RecipeNotFoundException;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;
import pl.healthyprogrammer.web.mapper.recipe.RecipeMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private GetRecipeService getRecipeService;

    private UUID recipeId;
    private Recipe recipe;
    private RecipeResponse expectedResponse;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        recipe = new Recipe();
        recipe.setId(recipeId);
        expectedResponse = new RecipeResponse();
        expectedResponse.setId(recipeId);
    }

    @Test
    void shouldReturnRecipeIfExists() {
        //given
        when(recipeRepository.findByIdWithIngredients(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeMapper.mapToDto(recipe)).thenReturn(expectedResponse);

        //when
        RecipeResponse actualResponse = getRecipeService.getRecipeById(recipeId);

        //then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        assertEquals(recipeId, actualResponse.getId());

        verify(recipeRepository, times(1)).findByIdWithIngredients(recipeId);
        verify(recipeMapper, times(1)).mapToDto(recipe);
    }

    @Test
    void shouldThrowRecipeNotFoundException() {
        //given
        when(recipeRepository.findByIdWithIngredients(recipeId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(
                RecipeNotFoundException.class,
                () -> getRecipeService.getRecipeById(recipeId));

        verify(recipeRepository, times(1)).findByIdWithIngredients(recipeId);
        verify(recipeMapper, never()).mapToDto(any());

    }

    @Test
    void shouldThrowUnexpectedExceptionWhenFetchingFails() {
        // given
        when(recipeRepository.findByIdWithIngredients(recipeId)).thenThrow(new RuntimeException("Database error"));

        // when & then
        assertThrows(
                RuntimeException.class,
                () -> getRecipeService.getRecipeById(recipeId));

        verify(recipeRepository, times(1)).findByIdWithIngredients(recipeId);
        verify(recipeMapper, never()).mapToDto(any());
    }
}