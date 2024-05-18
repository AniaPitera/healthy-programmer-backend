package pl.healthyprogrammer.core.model.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    @Query("SELECT r FROM Recipe r JOIN FETCH r.recipeIngredients WHERE r.id = :recipeId")
    Optional<Recipe> findByIdWithIngredients(@Param("recipeId") UUID recipeId);

    @Query("SELECT r FROM Recipe r JOIN FETCH r.recipeIngredients")
    Page<Recipe> findAllWithIngredients(Pageable pageable);
}
