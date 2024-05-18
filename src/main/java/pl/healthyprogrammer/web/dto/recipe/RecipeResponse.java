package pl.healthyprogrammer.web.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RecipeResponse {
    private UUID id;
    private String title;
    private String description;
    private String instructions;
}
