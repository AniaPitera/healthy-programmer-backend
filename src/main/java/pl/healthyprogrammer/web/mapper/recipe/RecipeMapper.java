package pl.healthyprogrammer.web.mapper.recipe;

import org.mapstruct.Mapper;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeResponse mapToDto(Recipe recipe);
}
