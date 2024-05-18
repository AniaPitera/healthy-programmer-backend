package pl.healthyprogrammer.web.dto.workout;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import pl.healthyprogrammer.core.model.workout.Workout;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResponse {
    private UUID id;
    private String title;
    private String thumbnail;
    private String channelTitle;
    @Enumerated(EnumType.STRING)
    private Workout.DifficultyLevel difficultyLevel;
    @Enumerated(EnumType.STRING)
    private Workout.ExerciseType exerciseType;
}