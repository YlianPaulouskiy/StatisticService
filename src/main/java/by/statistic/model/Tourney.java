package by.statistic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "tourneys")
@AllArgsConstructor
@NoArgsConstructor
public class Tourney {

    @Id
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Column(name = "win_rate", nullable = false,  columnDefinition = "decimal(4,1) default 100.0")
    private BigDecimal winRate = new BigDecimal("100.0");
    @Column(name = "losses", nullable = false, columnDefinition = "decimal(5,2) default 0.00")
    private BigDecimal losses = new BigDecimal("0.00");

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "sport_type_id", referencedColumnName = "id", nullable = false)
    private SportType sportType;

    @OneToMany(mappedBy = "tourney")
    private List<Match> matches;

}
