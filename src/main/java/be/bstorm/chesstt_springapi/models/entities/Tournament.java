package be.bstorm.chesstt_springapi.models.entities;

import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ToString(of = {"id", "name", "location", "createdAt"})
@EqualsAndHashCode(of = {"id", "name", "location", "createdAt"})
@Entity
@Table(name = "TOURNAMENT")
public class Tournament {

    @Getter
    @Setter
    @Column(name = "ID_TOURNAMENT")
    @Id
    private UUID id;

    @Getter
    @Setter
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Getter
    @Setter
    @Column(name = "LOCATION", nullable = true, length = 100)
    private String location;

    @Getter
    @Setter
    @Column(name = "MIN_PLAYER", nullable = false)
    @Range(min = 2)
    private int minPlayer;

    @Getter
    @Setter
    @Column(name = "MAX_PLAYER", nullable = false)
    @Range(min = 2)
    private int maxPlayer;

    @Getter
    @Setter
    @Column(name = "MIN_ELO", nullable = false)
    @Range(min = 0, max = 3000)
    private int minELO;

    @Getter
    @Setter
    @Column(name = "MAX_ELO", nullable = false)
    @Range(min = 0, max = 3000)
    private int maxElo;

    @Getter
    @Setter
    @Column(name = "CATEGORIES", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<TournamentCategory> categories;

    @Getter
    @Setter
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private TournamentStatus status;

    @Getter
    @Setter
    @Column(name = "IS_WOMEN_ONLY", nullable = false)
    private boolean isWomenOnly;

    @Getter
    @Setter
    @Column(name = "CURRENT_ROUND", nullable = false)
    @Range(min = 0)
    private int currentRound;

    @Getter
    @Setter
    @Column(name = "END_OF_REGISTRATION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endOfRegistrationDate;

    @Getter
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Getter
    @Column(name = "UPDATED_AT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "REGISTRATION",
            joinColumns = @JoinColumn(name = "ID_TOURNAMENT"),
            inverseJoinColumns = @JoinColumn(name = "ID_USER"))
    private Set<User> players;

    public Tournament() {
        players = new HashSet<>();
    }

    public Tournament(
            UUID id,
            String name,
            String location,
            int minPlayer,
            int maxPlayer,
            int minELO,
            int maxElo,
            List<TournamentCategory> categories,
            TournamentStatus status,
            boolean isWomenOnly,
            int currentRound,
            LocalDateTime endOfRegistrationDate) {
        this();
        this.id = id;
        this.name = name;
        this.location = location;
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
        this.minELO = minELO;
        this.maxElo = maxElo;
        this.categories = categories;
        this.status = status;
        this.isWomenOnly = isWomenOnly;
        this.currentRound = currentRound;
        this.endOfRegistrationDate = endOfRegistrationDate;
    }

    public Set<User> getPlayers() {
        return Set.copyOf(players);
    }

    public void addPlayer(User player) {
        this.players.add(player);
    }

    public void removePlayer(User player) {
        this.players.remove(player);
    }
}
