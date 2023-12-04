package be.bstorm.chesstt_springapi.models.entities;

import be.bstorm.chesstt_springapi.models.enums.MatchResult;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@ToString(of = {"id", "round", "result"})
@EqualsAndHashCode(of = {"id", "round", "result"})
@Entity
@Table(name = "MATCH")
public class Match {

    @Getter
    @Setter
    @Column(name = "ID_MATCH")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "ROUND")
    @Range(min = 1)
    private int round;

    @Getter
    @Setter
    @Column(name = "RESULT", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchResult result;

    @Getter
    @Setter
    @ManyToOne(
            fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TOURNAMENT", nullable = false)
    private Tournament tournament;

    @Getter
    @Setter
    @ManyToOne(
            fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_WHITE_PLAYER", nullable = false)
    private User whitePlayer;

    @Getter
    @Setter
    @ManyToOne(
            fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_BLACK_PLAYER", nullable = false)
    private User blackPlayer;
}
