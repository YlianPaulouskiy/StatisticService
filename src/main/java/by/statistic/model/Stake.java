package by.statistic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@Entity
@Table(name = "stakes")
@AllArgsConstructor
@NoArgsConstructor
public class Stake {

    @Id
    private Long id;

    @Column(name = "side_name", nullable = false)
    private String sideName;
    @Column(name = "ratio", nullable = false)
    private BigDecimal ratio;
    @Column(name = "is_winner", nullable = false, columnDefinition = "boolean default false")
    private Boolean isWinner = false;

    @ManyToOne
    @JoinColumn(name = "stake_type_id", referencedColumnName = "id", nullable = false)
    private StakeType stakeType;

}
