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
@Table(name = "sport-types")
@AllArgsConstructor
@NoArgsConstructor
public class SportType {

    @Id
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

}
