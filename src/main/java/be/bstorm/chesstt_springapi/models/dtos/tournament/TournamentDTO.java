package be.bstorm.chesstt_springapi.models.dtos.tournament;

import be.bstorm.chesstt_springapi.models.business.TournamentResult;
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
        Boolean isRegistered,
        Boolean canRegister
) {

    public static TournamentDTO fromBusiness(TournamentResult t){
        TournamentDTO dto = new TournamentDTO(
                t.getTournament().getId(),
                t.getTournament().getName(),
                t.getTournament().getLocation(),
                t.getTournament().getPlayers().size(),
                t.getTournament().getMinPlayers(),
                t.getTournament().getMaxPlayers(),
                t.getTournament().getMinElo(),
                t.getTournament().getMaxElo(),
                t.getTournament().getCategories(),
                t.getTournament().getStatus(),
                t.getTournament().getEndOfRegistrationDate(),
                t.getTournament().getCurrentRound(),
                t.isRegistered(),
                t.isCanRegister());
        return dto;
    }
}
