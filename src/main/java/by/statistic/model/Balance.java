package by.statistic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "balance")
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    @Column(name = "default_bet")
    private BigDecimal defaultBet;
    @Column(name = "tourney_count")
    private Long tourneyCount;

    @OneToMany(mappedBy = "balance", cascade = CascadeType.ALL)
    private List<Change> changes;
}
