package be.bstorm.chesstt_springapi.services.impl;

import be.bstorm.chesstt_springapi.exceptions.match.MatchException;
import be.bstorm.chesstt_springapi.exceptions.match.MatchNotFoundException;
import be.bstorm.chesstt_springapi.exceptions.tournament.TournamentNotFoundException;
import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.enums.MatchResult;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;
import be.bstorm.chesstt_springapi.repositories.MatchRepository;
import be.bstorm.chesstt_springapi.repositories.TournamentRepository;
import be.bstorm.chesstt_springapi.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;

    @Override
    public void updateResult(Long id, MatchResult result) {

        Match match = matchRepository.findMatchWithTournament(id).orElseThrow(MatchNotFoundException::new);
        if(!match.getTournament().getStatus().equals(TournamentStatus.IN_PROGRESS)){
            throw new MatchException("Cannot update a match the tournament is not in progress");
        }
        if(match.getRound() != match.getTournament().getCurrentRound()){
            throw new MatchException("Cannot update a match if it is not in the current round");
        }
        match.setResult(result);
        matchRepository.save(match);
    }

    @Override
    public Set<Match> findByTournamentAndRound(UUID tournamentId, int round) {
        Tournament t = tournamentRepository.findById(tournamentId).orElseThrow(TournamentNotFoundException::new);
        return matchRepository.getMatchesByTournamentIdAndRound(tournamentId,round);
    }
}
