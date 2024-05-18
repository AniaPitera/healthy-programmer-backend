package pl.healthyprogrammer.web.dto.recipe_ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.healthyprogrammer.web.dto.ingredient.IngredientResponse;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeIngredientResponse {
    private UUID id;
    private double quantity;
    private String unit;
    private IngredientResponse ingredient;
}
