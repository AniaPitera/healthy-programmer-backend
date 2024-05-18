package pl.healthyprogrammer.web.dto.recipe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.healthyprogrammer.web.dto.recipe_ingredient.RecipeIngredientRequest;

import java.util.Set;

@Data
@AllArgsConstructor
public class CreateRecipeRequest {
    @NotBlank
    @Size(max = 255)
    private String title;
    private String description;
    @NotBlank
    private String instructions;
    @NotEmpty
    @Valid
    private Set<RecipeIngredientRequest> recipeIngredients;
}
