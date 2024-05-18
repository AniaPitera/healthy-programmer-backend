package pl.healthyprogrammer.web.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class IngredientResponse {
    private UUID id;
    private String name;
    private double caloriesPerUnit;
}
