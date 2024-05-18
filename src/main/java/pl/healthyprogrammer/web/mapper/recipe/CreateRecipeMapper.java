package pl.healthyprogrammer.web.mapper.recipe;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.CreateRecipeResponse;

@Mapper(componentModel = "spring")
public interface CreateRecipeMapper {
    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "recipeIngredients", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "lastModifiedAt", ignore = true)
    })
    Recipe mapToEntity(CreateRecipeRequest request);

    CreateRecipeResponse mapToDto(Recipe recipe);
}
