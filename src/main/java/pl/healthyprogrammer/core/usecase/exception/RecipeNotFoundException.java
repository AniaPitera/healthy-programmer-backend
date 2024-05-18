package pl.healthyprogrammer.core.usecase.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecipeNotFoundException extends RuntimeException {
    private final String message;
}
