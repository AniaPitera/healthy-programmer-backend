package pl.healthyprogrammer.web.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import pl.healthyprogrammer.web.dto.workout.WorkoutResponse;
import pl.healthyprogrammer.core.model.workout.Workout;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutResponse mapToDto(Workout workout);

    default Page<WorkoutResponse> mapToDto(Page<Workout> workoutPage) {
        return workoutPage.map(this::mapToDto);
    }
}
