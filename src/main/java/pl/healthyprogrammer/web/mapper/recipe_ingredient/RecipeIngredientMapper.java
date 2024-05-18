package pl.healthyprogrammer.web.mapper.recipe_ingredient;

import org.mapstruct.Mapper;
import pl.healthyprogrammer.core.model.recipe_ingredient.RecipeIngredient;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientResponse;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {
    RecipeIngredientResponse mapToDto(RecipeIngredient recipeIngredient);
}
