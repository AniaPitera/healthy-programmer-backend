package pl.healthyprogrammer.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import pl.healthyprogrammer.dto.workout.WorkoutResponse;
import pl.healthyprogrammer.model.workout.Workout;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutResponse mapToDto(Workout workout);

    default Page<WorkoutResponse> mapToDto(Page<Workout> workoutPage) {
        return workoutPage.map(this::mapToDto);
    }
}
