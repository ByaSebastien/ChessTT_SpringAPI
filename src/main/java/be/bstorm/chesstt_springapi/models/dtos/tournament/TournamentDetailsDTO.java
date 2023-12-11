package be.bstorm.chesstt_springapi.models.dtos.tournament;

import be.bstorm.chesstt_springapi.models.business.TournamentDetailsResult;
import be.bstorm.chesstt_springapi.models.dtos.match.MatchDTO;
import be.bstorm.chesstt_springapi.models.dtos.user.UserDTO;
import be.bstorm.chesstt_springapi.models.enums.TournamentCategory;
import be.bstorm.chesstt_springapi.models.enums.TournamentStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record TournamentDetailsDTO(
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
        Boolean canRegister,
        Boolean canStart,
        Boolean canValidate,
        Set<UserDTO> players,
        Set<MatchDTO> matches
) {
    public static TournamentDetailsDTO fromBusiness(TournamentDetailsResult t){
        TournamentDetailsDTO dto = new TournamentDetailsDTO(
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
                t.isCanRegister(),
                t.canStart,
                t.canValidateRound,
                t.getTournament().getPlayers().stream().map(UserDTO::fromEntity).collect(Collectors.toSet()),
                t.getMatches().stream().map(MatchDTO::fromEntity).collect(Collectors.toSet()));
        return dto;
    }
}
