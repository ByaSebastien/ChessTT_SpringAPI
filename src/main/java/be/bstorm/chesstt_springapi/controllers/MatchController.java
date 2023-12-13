package be.bstorm.chesstt_springapi.controllers;

import be.bstorm.chesstt_springapi.models.dtos.match.MatchDTO;
import be.bstorm.chesstt_springapi.models.dtos.match.MatchResultDTO;
import be.bstorm.chesstt_springapi.models.entities.Match;
import be.bstorm.chesstt_springapi.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateResult(
            @PathVariable Long id,
            @RequestBody MatchResultDTO dto
            ){
        matchService.updateResult(id,dto.result());
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<MatchDTO>> getByTournamentAndRound(
            @RequestParam UUID tournamentId,
            @RequestParam int round
            ){
        List<MatchDTO> matches = matchService.findByTournamentAndRound(tournamentId, round)
                .stream()
                .map(MatchDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(matches);
    }
}
