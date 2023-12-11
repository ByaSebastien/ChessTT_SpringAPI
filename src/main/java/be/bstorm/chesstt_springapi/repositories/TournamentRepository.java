package be.bstorm.chesstt_springapi.repositories;

import be.bstorm.chesstt_springapi.models.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, UUID>, JpaSpecificationExecutor<Tournament> {

    @Query("select t " +
            "from Tournament t " +
            "join t.players u " +
            "where u.id = :playerId")
    Set<Tournament> getTournamentsByPlayerId(UUID playerId);

    @Query("select t " +
            "from Tournament t " +
            "join t.players " +
            "where t.id = :tournamentId")
    Optional<Tournament> findTournamentWithPlayers(UUID tournamentId);
}
