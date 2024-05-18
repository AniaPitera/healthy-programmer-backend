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
public class UpdateRecipeIngredientRequest {
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Double quantity;
    @NotBlank(message = "Unit cannot be blank")
    @Size(max = 50, message = "Unit cannot exceed 50 characters")
    private String unit;
    @NotNull(message = "Ingredient ID is required")
    private UUID ingredientId;
}
