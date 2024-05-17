package pl.healthyprogrammer.entrypoint.workout;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.healthyprogrammer.dto.workout.WorkoutResponse;
import pl.healthyprogrammer.usecase.workout.GetWorkoutsUseCase;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    private final GetWorkoutsUseCase getWorkoutsUseCase;

    @GetMapping
    public ResponseEntity<Page<WorkoutResponse>> getWorkouts(Pageable pageable) {
        return ResponseEntity.ok(getWorkoutsUseCase.getWorkouts(pageable));
    }
}
