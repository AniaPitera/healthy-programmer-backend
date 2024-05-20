package pl.healthyprogrammer.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientResponse;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private UUID id;
    private String title;
    private String description;
    private String instructions;
    private Set<RecipeIngredientResponse> recipeIngredients;
}
