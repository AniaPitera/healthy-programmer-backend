package pl.healthyprogrammer.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.healthyprogrammer.web.dto.recipe_ingredient.UpdateRecipeIngredientResponse;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateRecipeResponse {
    private UUID id;
    private String title;
    private String description;
    private String instructions;
    private Set<UpdateRecipeIngredientResponse> recipeIngredients;
}
