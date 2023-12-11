package be.bstorm.chesstt_springapi.models.dtos.match;

import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.models.enums.MatchResult;

import java.util.UUID;

public record MatchDTO(
        Long id,
        UUID tournamentId,
        UUID whiteId,
        UUID blackId,
        MatchResult result,
        String whiteName,
        String blackName,
        int round
) {
    public static MatchDTO fromEntity(Match m){
        return new MatchDTO(
                m.getId(),
                m.getTournament().getId(),
                m.getWhitePlayer().getId(),
                m.getBlackPlayer().getId(),
                m.getResult(),
                m.getWhitePlayer().getUsername(),
                m.getBlackPlayer().getUsername(),
                m.getRound());
    }
}
