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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private DeleteRecipeService deleteRecipeService;

    private UUID recipeId;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        recipe = new Recipe();
        recipe.setId(recipeId);
    }

    @Test
    void shouldDeleteRecipeIfExists() {
        //given
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        doNothing().when(recipeRepository).delete(recipe);

        //when
        deleteRecipeService.deleteRecipeById(recipeId);

        //then
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).delete(recipe);
    }

    @Test
    void shouldThrowRecipeNotFoundException() {
        //given
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        //when
        assertThrows(
                RecipeNotFoundException.class,
                () -> deleteRecipeService.deleteRecipeById(recipeId));

        //then
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, never()).delete(any());

    }

    @Test
    void shouldThrowUnexpectedExceptionWhenDeletionFails() {
        // given
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        doThrow(new RuntimeException("Database error")).when(recipeRepository).delete(recipe);

        // when & then
        assertThrows(
                RuntimeException.class,
                () -> deleteRecipeService.deleteRecipeById(recipeId));

        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).delete(recipe);
    }
}