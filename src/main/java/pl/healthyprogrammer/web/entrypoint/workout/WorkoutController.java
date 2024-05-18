package pl.healthyprogrammer.web.entrypoint.workout;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.healthyprogrammer.web.dto.workout.WorkoutResponse;
import pl.healthyprogrammer.core.usecase.workout.GetWorkoutsService;
import pl.healthyprogrammer.core.usecase.workout.WorkoutService;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    private final GetWorkoutsService getWorkoutsService;
    private final WorkoutService workoutService;

    @GetMapping("/add-from-youtube")
    public ResponseEntity<Void> addWorkoutsFromYouTube(@RequestParam String query, @RequestParam long maxResults) {
        workoutService.addWorkoutsFromYouTube(query, maxResults);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<WorkoutResponse>> getWorkouts(Pageable pageable) {
        return ResponseEntity.ok(getWorkoutsService.getWorkouts(pageable));
    }
}
