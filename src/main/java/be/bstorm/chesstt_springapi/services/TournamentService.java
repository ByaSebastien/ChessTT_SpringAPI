package be.bstorm.chesstt_springapi.services;

import be.bstorm.chesstt_springapi.models.business.TournamentDetailsResult;
import be.bstorm.chesstt_springapi.models.business.TournamentResult;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TournamentService {

    Tournament create(Tournament t);
    void delete(UUID id);
    List<TournamentResult> findTournamentsWithCriteria(
      String name,
      Set<TournamentCategory> categories,
      TournamentStatus status,
      Boolean isWomanOnly,
      User user,
      int page
    );
    long count(
            String name,
            Set<TournamentCategory> categories,
            TournamentStatus status,
            Boolean isWomanOnly
    );

    TournamentDetailsResult findOne(UUID tournamentId, User user);
}
