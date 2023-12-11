package be.bstorm.chesstt_springapi.controllers;

import be.bstorm.chesstt_springapi.models.dtos.tournament.TournamentDTO;
import be.bstorm.chesstt_springapi.models.dtos.tournament.TournamentIndexDTO;
import be.bstorm.chesstt_springapi.models.entities.Tournament;
import be.bstorm.chesstt_springapi.models.entities.User;
import be.bstorm.chesstt_springapi.models.forms.tournament.TournamentForm;
import be.bstorm.chesstt_springapi.models.forms.tournament.TournamentSearchForm;
import be.bstorm.chesstt_springapi.services.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid TournamentForm form,
            UriComponentsBuilder uriBuilder
            ){

        Tournament createdTournament = tournamentService.create(form.toEntity());
        URI location = uriBuilder.path("tournament/{id}").buildAndExpand(createdTournament.getId()).toUri();
        //TODO map to DTO
        return ResponseEntity.created(location).body(createdTournament);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ){
        tournamentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<TournamentIndexDTO> get(
            Authentication authentication,
            @ModelAttribute TournamentSearchForm form
            ){

        Long total = tournamentService.count(
                form.name(),
                form.categories(),
                form.status(),
                form.isWomenOnly()
        );
        List<TournamentDTO> tournaments = tournamentService.findTournamentsWithCriteria(
                form.name(),
                form.categories(),
                form.status(),
                form.isWomenOnly(),
                authentication == null ? null : (User) authentication.getPrincipal(),
                form.page()
        ).stream().map(TournamentDTO::fromBusiness).toList();

        TournamentIndexDTO result = new TournamentIndexDTO(total,tournaments);
        return ResponseEntity.status(200).header("Total",total.toString()).body(result);
    }
}
