package by.statistic.repository;

import by.statistic.model.Tourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourneyRepository extends JpaRepository<Tourney, Long> {

    boolean existsByTitle(String title);

}
