package by.statistic.repository;

import by.statistic.model.Change;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeRepository extends JpaRepository<Change, Long> {

    List<Change> findAllByMatchTourneyTitle(String tourneyTitle);

}
