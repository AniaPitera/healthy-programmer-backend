package pl.healthyprogrammer.core.usecase.workout;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.healthyprogrammer.web.dto.workout.WorkoutResponse;
import pl.healthyprogrammer.web.mapper.WorkoutMapper;
import pl.healthyprogrammer.core.model.workout.WorkoutRepository;

@Service
@RequiredArgsConstructor
public class GetWorkoutsService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;

    public Page<WorkoutResponse> getWorkouts(Pageable pageable) {
        return Option.of(workoutRepository.findAll(pageable))
                .map(workoutMapper::mapToDto)
                .getOrElse(Page.empty());
    }
}
