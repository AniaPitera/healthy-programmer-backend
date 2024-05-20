package pl.healthyprogrammer.core.usecase.recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.core.model.recipe.RecipeRepository;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;
import pl.healthyprogrammer.web.mapper.recipe.RecipeMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRecipesServiceTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private GetRecipesService getRecipesService;

    private Pageable pageable;
    private Page<Recipe> recipePage;
    private Page<RecipeResponse> recipeResponsePage;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5);

        Recipe recipe1 = new Recipe();
        recipe1.setId(UUID.randomUUID());
        Recipe recipe2 = new Recipe();
        recipe2.setId(UUID.randomUUID());

        RecipeResponse recipeResponse1 = new RecipeResponse();
        recipeResponse1.setId(recipe1.getId());
        RecipeResponse recipeResponse2 = new RecipeResponse();
        recipeResponse2.setId(recipe2.getId());

        List<Recipe> recipes = List.of(recipe1, recipe2);
        List<RecipeResponse> recipeResponses = List.of(recipeResponse1, recipeResponse2);

        recipePage = new PageImpl<>(recipes, pageable, recipes.size());
        recipeResponsePage = new PageImpl<>(recipeResponses, pageable, recipeResponses.size());

    }

    @Test
    void shouldReturnPageOfRecipes() {
        //given
        when(recipeRepository.findAllWithIngredients(pageable)).thenReturn(recipePage);
        when(recipeMapper.mapPageToDto(recipePage)).thenReturn(recipeResponsePage);

        //when
        Page<RecipeResponse> actualResponse = getRecipesService.getRecipes(pageable);

        //then
        assertNotNull(actualResponse);
        assertEquals(recipeResponsePage, actualResponse);
        assertEquals(pageable.getPageNumber(), actualResponse.getNumber());
        assertEquals(pageable.getPageSize(), actualResponse.getSize());

        verify(recipeRepository, times(1)).findAllWithIngredients(pageable);
        verify(recipeMapper, times(1)).mapPageToDto(recipePage);
    }

    @Test
    void shouldThrowUnexpectedExceptionWhenFetchingFails() {
        //given
        when(recipeRepository.findAllWithIngredients(pageable)).thenThrow(new RuntimeException("Error fetching recipes"));

        //when
        assertThrows(
                RuntimeException.class,
                () -> getRecipesService.getRecipes(pageable));

        //then
        verify(recipeRepository, times(1)).findAllWithIngredients(pageable);
        verify(recipeMapper, never()).mapPageToDto(any());
    }
}