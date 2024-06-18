package by.statistic.repository;

import by.statistic.model.Stake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StakeTypeRepository extends JpaRepository<Stake, Long> {
}
