package pl.healthyprogrammer.web.dto.recipe_ingredient;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeIngredientRequest {
    @NotNull
    @Min(0)
    private Double quantity;
    @NotBlank
    @Size(max = 50)
    private String unit;
    @NotNull
    private UUID ingredientId;
}
