package pl.healthyprogrammer.core.usecase.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IngredientNotFoundException extends RuntimeException {
    private final String message;
}
