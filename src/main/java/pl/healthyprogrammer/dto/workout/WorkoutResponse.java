package pl.healthyprogrammer.dto.workout;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import pl.healthyprogrammer.model.workout.Workout;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResponse {
    private UUID id;
    private String title;
    private String link;
    private String channelTitle;
    @Enumerated(EnumType.STRING)
    private Workout.DifficultyLevel difficultyLevel;
    @Enumerated(EnumType.STRING)
    private Workout.ExerciseType exerciseType;
}