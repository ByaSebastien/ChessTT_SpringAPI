package be.bstorm.chesstt_springapi.models.dtos.tournament;

import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TournamentDTO(

        UUID id,
        String name,
        String location,
        int playersCount,
        Integer minPlayer,
        Integer maxPlayer,
        Integer minElo,
        Integer maxElo,
        Set<TournamentCategory> categories,
        TournamentStatus status,
        LocalDateTime endOfRegistrationDate,
        int currentRound,
        boolean isRegistered,
        boolean canRegister
) {
}
