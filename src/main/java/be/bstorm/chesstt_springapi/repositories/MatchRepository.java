package be.bstorm.chesstt_springapi.repositories;

import be.bstorm.chesstt_springapi.models.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("select m from Match m join m.tournament t where t.id = :tournamentId")
    Set<Match> getMatchesByTournamentId(UUID tournamentId);

    @Query("select m from Match m join m.tournament t where t.id = :tournamentId and m.round = :round")
    Set<Match> getMatchesByTournamentIdAndRound(UUID tournamentId, int round);

    @Query("select m " +
            "from Match m " +
            "join m.whitePlayer w " +
            "join m.blackPlayer b " +
            "where w.id = :playerId or b.id = :playerId")
    Set<Match> getMatchesByPlayerId(UUID playerId);

    @Query("select m from Match m join m.tournament where m.id = :id")
    Optional<Match> findMatchWithTournament(Long id);
}
