package by.statistic.model;

import by.statistic.model.converter.LocalDateTimeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "matches",
        uniqueConstraints = {@UniqueConstraint(columnNames = { "full_name", "date" })})
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    @Id
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "first_team_id", referencedColumnName = "id", nullable = false)
    private Team firstTeam;

    @ManyToOne
    @JoinColumn(name = "second_team_id", referencedColumnName = "id", nullable = false)
    private Team secondTeam;

    @ManyToOne
    @JoinColumn(name = "tourney_id", referencedColumnName = "id", nullable = false)
    private Tourney tourney;

    @OneToOne
    @JoinColumn(name = "stake_id", referencedColumnName = "id", nullable = false)
    private Stake stake;

}
