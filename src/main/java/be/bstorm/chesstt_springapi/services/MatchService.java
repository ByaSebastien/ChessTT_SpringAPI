package be.bstorm.chesstt_springapi.services;

import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.enums.MatchResult;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MatchService {

    void updateResult(Long id, MatchResult result);

    Set<Match> findByTournamentAndRound(UUID tournamentId, int round);
}
