package pl.healthyprogrammer.web.mapper.recipe;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import pl.healthyprogrammer.core.model.recipe.Recipe;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeRequest;
import pl.healthyprogrammer.web.dto.recipe.UpdateRecipeResponse;

@Mapper(componentModel = "spring")
public interface UpdateRecipeMapper {
    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "recipeIngredients", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "lastModifiedAt", ignore = true)
    })
    void updateRecipeUsingRequest(UpdateRecipeRequest request, @MappingTarget Recipe recipe);

    UpdateRecipeResponse mapToDto(Recipe recipe);
}
