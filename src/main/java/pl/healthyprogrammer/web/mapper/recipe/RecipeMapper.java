package pl.healthyprogrammer.web.mapper.recipe;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.web.dto.recipe.RecipeResponse;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeResponse mapToDto(Recipe recipe);

    default Page<RecipeResponse> mapPageToDto(Page<Recipe> recipePage) {
        return recipePage.map(this::mapToDto);
    }
}
