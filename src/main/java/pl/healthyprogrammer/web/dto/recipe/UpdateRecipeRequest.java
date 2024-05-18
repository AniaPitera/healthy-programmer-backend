package pl.healthyprogrammer.web.dto.recipe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.healthyprogrammer.web.dto.recipe_ingredient.UpdateRecipeIngredientRequest;

import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateRecipeRequest {
    @NotBlank(message = "Recipe title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;
    private String description;
    @NotBlank(message = "Instructions are required")
    private String instructions;
    @NotEmpty(message = "Recipe must have at least one ingredient")
    @Valid
    private Set<UpdateRecipeIngredientRequest> recipeIngredients;
}
