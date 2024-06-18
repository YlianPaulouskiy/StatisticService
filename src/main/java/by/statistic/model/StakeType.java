package by.statistic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "stake-types")
@AllArgsConstructor
@NoArgsConstructor
public class StakeType {

    @Id
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
