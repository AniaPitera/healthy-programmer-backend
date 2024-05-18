package pl.healthyprogrammer.core.model.workout;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.healthyprogrammer.core.common.AuditBase;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "workouts")
public class Workout extends AuditBase {

    private String title;
    private String thumbnail;
    private String channelTitle;
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workout workout)) return false;
        return Objects.equals(title, workout.title) && Objects.equals(thumbnail, workout.thumbnail) && Objects.equals(channelTitle, workout.channelTitle) && difficultyLevel == workout.difficultyLevel && exerciseType == workout.exerciseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, thumbnail, channelTitle, difficultyLevel, exerciseType);
    }

    public enum DifficultyLevel {
        EASY,
        MEDIUM,
        HARD
    }

    public enum ExerciseType {
        RELAXATION,
        WARM_UP,
        CARDIO,
        WORKOUT,
        STRETCHING
    }
}
