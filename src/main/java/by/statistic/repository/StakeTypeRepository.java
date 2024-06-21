package by.statistic.repository;

import by.statistic.model.StakeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StakeTypeRepository extends JpaRepository<StakeType, Long> {
}
