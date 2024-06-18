package by.statistic.model;

import by.statistic.model.converter.LocalDateTimeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "balance-changes")
@AllArgsConstructor
@NoArgsConstructor
public class Change {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "change", nullable = false, columnDefinition = "varchar(10) default '-'")
    private String change = "-";

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "match_id", referencedColumnName = "id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_id", referencedColumnName = "id")
    private Balance balance;

}
